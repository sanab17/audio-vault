package com.audiovault.specification;

import java.util.ArrayList;
import java.util.List;

import com.audiovault.dto.SearchCriteria;
import com.audiovault.model.Recording;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class RecordingSpecification {
    
    public static Specification<Recording> withCriteria(SearchCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getFileName() != null && !criteria.getFileName().isBlank()) {
                predicates.add(cb.like(
                    cb.lower(root.get("fileName")),
                    "%" + criteria.getFileName().toLowerCase() + "%"
                ));
            }

            if (criteria.getContentType() != null && !criteria.getContentType().isBlank()) {
                predicates.add(cb.equal(root.get("contentType"), criteria.getContentType()));
            }

            if (criteria.getMinDuration() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("duration"), criteria.getMinDuration()));
            }

            if (criteria.getMaxDuration() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("duration"), criteria.getMaxDuration()));
            }

            if (criteria.getMinFileSize() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("fileSize"), criteria.getMinFileSize()));
            }

            if (criteria.getMaxFileSize() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("fileSize"), criteria.getMaxFileSize()));
            }

            if (criteria.getUploadedAfter() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("uploadDate"), criteria.getUploadedAfter()));
            }

            if (criteria.getUploadedBefore() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("uploadDate"), criteria.getUploadedBefore()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
