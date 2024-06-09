package com.techpeak.hac.core.dtos;

import com.techpeak.hac.core.enums.InternalPhase;

import java.io.Serializable;

/**
 * DTO for {@link com.techpeak.hac.core.models.InternalRef}
 */

public record InternalRefDto(Long id,
                             InternalPhase currentPhase) implements Serializable {
}