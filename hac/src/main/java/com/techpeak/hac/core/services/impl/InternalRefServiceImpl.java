package com.techpeak.hac.core.services.impl;

import com.techpeak.hac.core.exception.NotFoundException;
import com.techpeak.hac.core.models.InternalRef;
import com.techpeak.hac.core.repositories.InternalRefRepository;
import com.techpeak.hac.core.services.InternalRefService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InternalRefServiceImpl implements InternalRefService {
    private final InternalRefRepository internalRefRepository;
    @Override
    public InternalRef getInternalRefById(Long id) {
       return internalRefRepository.findById(id).orElseThrow(()-> new NotFoundException("InternalRef not found with id: "+id));
    }
}
