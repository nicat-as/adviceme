package com.uniso.equso.dao.spec;

import com.uniso.equso.dao.entities.PostEntity;
import com.uniso.equso.dao.enums.Status;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.text.MessageFormat;
import java.util.List;

public final class PostEntitySpecification {
    private static final String TEXT = "text";
    private static final String CATEGORY = "category";
    private static final String STATUS = "status";
    private static final String CREATOR = "creator";
    private static final String WALL_USER = "wallUser";
    private static final String ID = "id";

    public static Specification<PostEntity> postContains(String exp) {
        return (root, query, builder) -> StringUtils.hasText(exp) ?
                builder.like(
                        builder.lower(root.get(TEXT)),
                        builder.literal(contains(exp))
                ) :
                builder.conjunction();
    }

    public static Specification<PostEntity> categoryNameContains(String exp) {
        return (root, query, builder) -> {
            if (StringUtils.hasText(exp)) {
                var categoryJoin = root.join(CATEGORY, JoinType.INNER);
                return builder.like(
                        builder.lower(categoryJoin.get(TEXT)),
                        builder.literal(contains(exp))
                );
            } else {
                return builder.conjunction();
            }
        };
    }

    public static Specification<PostEntity> creatorIdEqual(Long exp) {
        return (root, query, builder) -> {
            if (exp != null) {
                var creatorJoin = root.join(CREATOR, JoinType.INNER);
                return builder.equal(creatorJoin.get(ID), exp);
            } else {
                return builder.conjunction();
            }
        };
    }

    public static Specification<PostEntity> wallUserIdEqual(Long exp) {
        return (root, query, builder) -> {
            if (exp != null) {
                var creatorJoin = root.join(WALL_USER, JoinType.INNER);
                return builder.equal(creatorJoin.get(ID), exp);
            } else {
                return builder.conjunction();
            }
        };
    }


    public static Specification<PostEntity> checkStatus(Status status) {
        return (root, query, builder) -> builder.equal(root.get(STATUS), status);
    }


    public static Specification<PostEntity> excludeCategories(List<Long> excludedCategories) {
        return (root, query, builder) -> {
            if (excludedCategories != null) {
                return builder.not(root.get(CATEGORY).in(excludedCategories));
            } else {
                return builder.conjunction();
            }
        };
    }

    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }

}
