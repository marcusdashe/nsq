/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.services;


import lombok.extern.slf4j.Slf4j;
import org.cstemp.nsq.models.relational.Option;
import org.cstemp.nsq.payload.OptionPayload;
import org.cstemp.nsq.payload.PagedResponse;
import org.cstemp.nsq.repos.OptionRepository;
import org.cstemp.nsq.util.NinasUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 *
 * @author chibuezeharry & MarcusDashe
 */

@Service
@Slf4j
public class OptionService {

    @Autowired
    private OptionRepository optionRepository;

    public Boolean existsByName(String name) {
        return optionRepository.existsByName(name).orElse(false);
    }

    public Boolean existsByNameAndGroupAndValue(String name, String group, String value) {
        return optionRepository.existsByNameAndGroupNameAndOptionValue(name, group, value).orElse(false);
    }

    public Boolean existsByNameAndValue(String name, String value) {
        return optionRepository.existsByNameAndOptionValue(name, value).orElse(false);
    }

    public List<OptionPayload.OptionResponse> getOptionsByName(String name) {

        List<OptionPayload.OptionResponse> optionResponses = new ArrayList<>();

        List<Option> options = optionRepository.findAllByName(name);

        options.forEach((option) -> {

            OptionPayload.OptionResponse optionResponse = new OptionPayload.OptionResponse();

            BeanUtils.copyProperties(option, optionResponse);

            optionResponses.add(optionResponse);
        });

        return optionResponses;

    }

    public List<OptionPayload.OptionResponse> getOptionsByGroup(String name, String group) {

        List<OptionPayload.OptionResponse> optionResponses = new ArrayList<>();

        List<Option> options = optionRepository.findByNameAndGroupName(name, group);

        options.forEach((option) -> {
            OptionPayload.OptionResponse optionResponse = new OptionPayload.OptionResponse();
            BeanUtils.copyProperties(option, optionResponse);
            optionResponses.add(optionResponse);
        });
        return optionResponses;
    }

    public OptionPayload.OptionResponse addOption(OptionPayload.OptionRequest optionRequest) {
        OptionPayload.OptionResponse optionResponse = null;
        if (optionRequest != null) {
            if (!existsByNameAndGroupAndValue(optionRequest.getName(), optionRequest.getGroup(), optionRequest.getValue())) {
                Option option = new Option();
                BeanUtils.copyProperties(optionRequest, option);
                Option result = optionRepository.save(option);
                if (result != null) {
                    optionResponse = new OptionPayload.OptionResponse();
                    BeanUtils.copyProperties(result, optionResponse);
                }
            }
        }
        return optionResponse;
    }

    public void addOptions(List<OptionPayload.OptionRequest> optionRequests) {
        if (optionRequests != null && optionRequests.size() > 0) {
            optionRequests.forEach((optionRequest) -> {

                if (!existsByNameAndGroupAndValue(optionRequest.getName(), optionRequest.getGroup(), optionRequest.getValue())) {
                    Option option = new Option();
                    BeanUtils.copyProperties(optionRequest, option);
                    optionRepository.save(option);
                }
            });
        }
    }

    public OptionPayload.OptionResponse updateOption(Long optionId, OptionPayload.OptionRequest optionRequest) {
        OptionPayload.OptionResponse optionResponse = null;
        Option option = optionRepository.findById(optionId).orElse(null);
        if (option != null) {
            if (optionRequest.getName() != null) {
                option.setName(optionRequest.getName());
            }
            if (optionRequest.getCode() != null) {
                option.setCode(optionRequest.getCode());
            }
            Option result = optionRepository.save(option);
            if (result != null) {
                optionResponse = new OptionPayload.OptionResponse();
                BeanUtils.copyProperties(result, optionResponse);
            }
        }
        return optionResponse;
    }

    public OptionPayload.OptionResponse getOption(Long optionId) {
        Option option = optionRepository.findById(optionId).orElse(null);
        OptionPayload.OptionResponse optionResponse = null;
        if (option != null) {
            optionResponse = new OptionPayload.OptionResponse();
            BeanUtils.copyProperties(option, optionResponse);
        }
        return optionResponse;
    }

    public PagedResponse<OptionPayload.OptionResponse> getOptions(int page, int size) {
        NinasUtil.validatePageNumberAndSize(page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        Page<Option> options = optionRepository.findAll(pageable);
        if (options.getNumberOfElements() == 0) {
            return new PagedResponse<>(false, "Options list is empty", Collections.emptyList(), options.getNumber(),
                    options.getSize(), options.getTotalElements(), options.getTotalPages(), options.isLast());
        }

        List<OptionPayload.OptionResponse> optionResponses = options.map(option -> {
            OptionPayload.OptionResponse optionResponse = new OptionPayload.OptionResponse();
            BeanUtils.copyProperties(option, optionResponse);
            return optionResponse;
        }).getContent();

        return new PagedResponse<>(true, "Options Retrieved Successfully", optionResponses, options.getNumber(),
                options.getSize(), options.getTotalElements(), options.getTotalPages(), options.isLast());
    }

    public List<OptionPayload.OptionResponse> getOptions() {
        List<OptionPayload.OptionResponse> optionResponses = new ArrayList<>();
        List<Option> options = optionRepository.findAll();

        options.forEach((option) -> {
            OptionPayload.OptionResponse optionResponse = new OptionPayload.OptionResponse();
            BeanUtils.copyProperties(option, optionResponse);
            optionResponses.add(optionResponse);
        });
        return optionResponses;
    }

    public Option getOptionById(Long optionId) {
        return optionRepository.findById(optionId).orElse(null);
    }

    public void deleteOption(Long optionId) {

        Option option = optionRepository.findById(optionId).orElse(null);

        if (option != null) {

            optionRepository.deleteById(optionId);
        }
    }

    public Map<String, Map<String, List<String>>> getOptionsMapping(List<String> optionNames) {

        List<String> optionNames1 = new ArrayList<>();

        optionNames.forEach((option) -> {

            optionNames1.add(option.toUpperCase());
        });

        List<Option> options = optionRepository.findByNameIn(optionNames1);

        Map<String, Map<String, List<String>>> optionsMap = new HashMap<>();

        options.forEach((option) -> {

            if (null == optionsMap.get(option.getName())) {
                optionsMap.put(option.getName(), new HashMap<>());
            }

            if (null == option.getGroupName()) {
                option.setGroupName("");
            }

            if (null == optionsMap.get(option.getName()).get(option.getGroupName())) {
                optionsMap.get(option.getName()).put(option.getGroupName(), new ArrayList<>());
            }

            optionsMap.get(option.getName()).get(option.getGroupName()).add(option.getOptionValue());

        });
        return optionsMap;
    }
}
