package com.techpeak.hac.purchase.mappers;

import com.techpeak.hac.core.dtos.InternalRefDto;
import com.techpeak.hac.core.models.InternalRef;

public class InternalRefMapper {
    private InternalRefMapper() {
    }
    public static InternalRefDto mapToInternalRefDto(InternalRef internalRef) {
        return new InternalRefDto(internalRef.getId(), internalRef.getCurrentPhase());
    }
}
