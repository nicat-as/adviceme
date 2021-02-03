package com.uniso.equso.dao.spec;

import com.uniso.equso.dao.entities.PostEntity;
import com.uniso.equso.dao.enums.Status;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.JoinType;
import java.text.MessageFormat;

public final class PostEntitySpecification {
    private static final String TEXT = "text";
    private static final String CATEGORY = "category";
    private static final String STATUS = "status";

    public static Specification<PostEntity> postContains(String exp) {
        return (root, query, builder) -> StringUtils.hasText(exp) ?
                builder.like(root.get(TEXT), contains(exp)) :
                builder.conjunction();
    }

    public static Specification<PostEntity> categoryNameContains(String exp) {
        return (root, query, builder) -> {
            if (StringUtils.hasText(exp)) {
                var categoryJoin = root.join(CATEGORY, JoinType.INNER);
                return builder.like(categoryJoin.get(TEXT), contains(exp));
            } else {
                return builder.conjunction();
            }
        };
    }

    public static Specification<PostEntity> checkStatus(Status status) {
        return (root, query, builder) -> builder.equal(root.get(STATUS), status);
    }

    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }
}
