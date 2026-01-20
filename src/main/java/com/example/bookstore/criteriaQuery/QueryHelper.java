package com.example.bookstore.criteriaQuery;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public final class QueryHelper {

    private QueryHelper() {}

    public static <T> Specification<T> globalSearch(
            String searchValue,
            List<String> fields
    ) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(searchValue)) {
                return cb.conjunction();
            }

            String likeValue = "%" + searchValue.toLowerCase() + "%";

            Predicate[] predicates = fields.stream()
                    .map(field ->
                            cb.like(
                                    cb.lower(root.get(field).as(String.class)),
                                    likeValue
                            )
                    )
                    .toArray(Predicate[] :: new);

            return cb.or(predicates);
        };
    }

    public static <R, Q> Predicate getPredicate(Root<R> root, Q criteriaObject, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        if (criteriaObject == null) {
            return cb.conjunction();
        }

        List<Predicate> predicates = new ArrayList<>();
        List<Field> fields = getAllFields(criteriaObject.getClass());
        {
            for (Field field: fields){
                Query q = field.getAnnotation(Query.class);
                if (q == null) continue;

                field.setAccessible(true);
                Object rawValue;
                try {
                    rawValue = field.get(criteriaObject);
                } catch (IllegalAccessException e) {
                    continue;
                }

                if (shouldSkip(rawValue, q.type())) continue;

                boolean hasJoin = StringUtils.hasText(q.joinName());
                if (q.distinct() && hasJoin) {
                    cq.distinct(true);
                }
                if (StringUtils.hasText(q.blurry())) {
                    String[] blurryFields = q.blurry().split(",");
                    List<Predicate> orPredicates = new ArrayList<>();

                    for (String bf : blurryFields) {
                        orPredicates.add(
                                cb.like(
                                        cb.lower(root.get(bf.trim()).as(String.class)),
                                        "%" + rawValue.toString().toLowerCase() + "%"
                                )
                        );
                    }
                    predicates.add(cb.or(orPredicates.toArray(new Predicate[0])));
                    continue;
                }

                From<?, ?> from = createJoinIfNeeded(root, q);
                String attributeName = StringUtils.hasText(q.propName()) ? q.propName() : field.getName();

                Predicate single = createPredicate(cb, from, attributeName, q.type(), rawValue, field);
                if (single != null) predicates.add(single);
            }
            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));

        }
    }

    private static Predicate buildBlurry(Root<?> root, CriteriaBuilder cb, String blurrySpec, String value) {
        String[] props = Arrays.stream(blurrySpec.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);

        if (props.length == 0) return null;
        String like = "%" + value.toLowerCase() + "%";

        List<Predicate> ors = new ArrayList<>();
        for (String prop : props) {
            Expression<String> expr = root.get(prop).as(String.class);
            ors.add(cb.like(cb.lower(expr), like));
        }
        return cb.or(ors.toArray(new Predicate[0]));
    }

    private static <R> From<?, ?> createJoinIfNeeded(Root<R> root, Query q) {
        if (!StringUtils.hasText(q.joinName())) {
            return root;
        }

        From<?, ?> from = root;
        for (String part : q.joinName().split(">")) {
            from = from.join(part.trim(), JoinType.LEFT);
        }
        return from;
    }

    @SuppressWarnings("unchecked")
    private static Predicate createPredicate(CriteriaBuilder cb,
                                             From<?, ?> from,
                                             String attributeName,
                                             Type type,
                                             Object value,
                                             Field field) {
        Path<?> path = from.get(attributeName);

        switch (type) {
            case EQUAL:
                return cb.equal(path, value);
            case NOT_EQUAL:
                return cb.notEqual(path, value);
            case IS_NULL:
                return cb.isNull(path);
            case NOT_NULL:
                return cb.isNotNull(path);
            case GREATER_THAN:
                return cb.greaterThan(path.as((Class<? extends Comparable>) field.getType()), (Comparable) value);
            case GREATER_THAN_OR_EQUAL:
                return cb.greaterThanOrEqualTo(path.as((Class<? extends Comparable>) field.getType()), (Comparable) value);
            case LESS_THAN:
                return cb.lessThan(path.as((Class<? extends Comparable>) field.getType()), (Comparable) value);
            case LESS_THAN_OR_EQUAL:
                return cb.lessThanOrEqualTo(path.as((Class<? extends Comparable>) field.getType()), (Comparable) value);
            case INNER_LIKE:
            case LEFT_LIKE:
            case RIGHT_LIKE:
                String s = value.toString();
                if (type == Type.INNER_LIKE) s = "%" + s + "%";
                else if (type == Type.LEFT_LIKE) s = "%" + s;
                else s = s + "%";
                return cb.like(cb.lower(path.as(String.class)), s.toLowerCase());
            case IN:
                if (value instanceof Collection) {
                    CriteriaBuilder.In<Object> in = cb.in(path);
                    ((Collection<?>) value).forEach(in::value);
                    return in;
                } else {
                    return null;
                }
            case BETWEEN:
                // expecting a List with two values (start, end). Support Date -> Timestamp conversion
                if (value instanceof List<?> list && list.size() == 2) {
                    Object start = list.get(0);
                    Object end = list.get(1);
                    if (start instanceof Date && end instanceof Date) {
                        Timestamp tsStart = toStartOfDay((Date) start);
                        Timestamp tsEnd = toEndOfDay((Date) end);
                        return cb.between(path.as(Timestamp.class), tsStart, tsEnd);
                    } else if (start instanceof Comparable && end instanceof Comparable) {
                        return cb.between(path.as((Class) start.getClass()), (Comparable) start, (Comparable) end);
                    }
                }
                return null;
            default:
                return null;
        }
    }

    private static Timestamp toStartOfDay(Date d) {
        LocalDate ld = Instant.ofEpochMilli(d.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime start = ld.atStartOfDay();
        return Timestamp.valueOf(start);
    }

    private static Timestamp toEndOfDay(Date d) {
        LocalDate ld = Instant.ofEpochMilli(d.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime end = ld.atTime(23,59,59);
        return Timestamp.valueOf(end);
    }


    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    private static boolean shouldSkip(Object val, Type type) {
        if (val == null) return !(type == Type.IS_NULL || type == Type.NOT_NULL);
        if (val instanceof String s) return s.isBlank();
        if (val instanceof Collection<?> c) return c.isEmpty();
        return false;
    }
}


