package com.example.LanguageSchool.Services;

import com.example.LanguageSchool.Models.Type;
import com.example.LanguageSchool.Repositories.ITypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TypeService {
    private ITypeRepository iTypeRepository;
    @Autowired
    public TypeService(ITypeRepository iTypeRepository){
        this.iTypeRepository=iTypeRepository;
    }
    public List<Type> getAllTypes(){
        return iTypeRepository.findAll();
    }

}