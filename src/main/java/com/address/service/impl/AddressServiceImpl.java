package com.address.service.impl;

import com.address.client.EmployeeClient;
import com.address.exception.CustomException;
import com.address.exception.ResourceNotFoundException;
import com.address.model.dto.AddressDto;
import com.address.model.dto.AddressRequest;
import com.address.model.dto.AddressRequestDto;
import com.address.model.dto.EmployeeDto;
import com.address.model.entity.Address;
import com.address.repository.AddressRepository;
import com.address.service.AddressService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AddressServiceImpl implements AddressService {

    Logger log = LoggerFactory.getLogger(AddressServiceImpl.class);

    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;
    private final EmployeeClient employeeClient;


    public AddressServiceImpl(AddressRepository addressRepository, ModelMapper modelMapper, EmployeeClient employeeClient) {
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
        this.employeeClient = employeeClient;
    }

    @Override
    public List<AddressDto> saveAddress(AddressRequest addressRequest) {
        // TODO : Check if employee exists
        EmployeeDto employee=employeeClient.getSingleEmployee(addressRequest.getEmpId());

        if(employee==null){
            throw new ResourceNotFoundException("Employee not found with id :" + addressRequest.getEmpId());

        }

        List<Address>listToSave=this.saveOrUpdateAddressRequest(addressRequest);

        List<Address> savedAddress = addressRepository.saveAll(listToSave);

        return savedAddress.stream().map(address -> modelMapper.map(address, AddressDto.class)).toList();
    }

    @Override
    public List<AddressDto> updateAddress(AddressRequest addressRequest) {

        EmployeeDto employee=employeeClient.getSingleEmployee(addressRequest.getEmpId());


        List<Address> addressByEmpId = addressRepository.findByEmpId(addressRequest.getEmpId());


        if (addressByEmpId.isEmpty()) {
            log.info("NO address found for employee id {}", addressRequest.getEmpId());
            log.info("Creating new address for employee id {} ", addressRequest.getEmpId());
        }

        List<Address>listToUpdate=this.saveOrUpdateAddressRequest(addressRequest);

        List<Long>upcomingNonNullIds=listToUpdate.stream().map(Address::getId).filter(Objects::nonNull).toList();
        List<Long>existingIds=addressByEmpId.stream().map(Address::getId).toList();

        List<Long>idsToDelete=existingIds.stream().filter(id-> !upcomingNonNullIds.contains(id)).toList();

        if(!idsToDelete.isEmpty()){
            addressRepository.deleteAllById(idsToDelete);
        }

        List<Address>updateAddress=addressRepository.saveAll(listToUpdate);

        return updateAddress.stream().map(address -> modelMapper.map(address,AddressDto.class)).toList();


    }

    @Override
    public AddressDto getSingleAddress(Long id) {

        Address address = addressRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found with id " + id
                        )
                );

        return modelMapper.map(address, AddressDto.class);
    }

    @Override
    public List<AddressDto> getAllAddress() {

        List<Address>all=addressRepository.findAll();
        if(all.isEmpty()){
            throw new ResourceNotFoundException("No address found");
        }

        List<AddressDto>addressDtos=new ArrayList<>();

        return all.stream().map(address -> modelMapper.map(address,AddressDto.class)).toList();

    }

    public void deleteAddress(Long id){
        Address address=addressRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Address not found  with id :" + id));

        addressRepository.delete(address);

    }

    @Override
    public List<AddressDto> getAddressByEmpId(Long id) {

        List<Address> addresses = addressRepository.findByEmpId(id);

        if (addresses.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Address not found with employee id " + id
            );
        }

        return addresses.stream()
                .map(address -> modelMapper.map(address, AddressDto.class))
                .toList();
    }

    private List<Address> saveOrUpdateAddressRequest(AddressRequest addressRequest) {
        List<Address> listToSave = new ArrayList<>();

        for (AddressRequestDto addressRequestDto : addressRequest.getAddressRequestDtoList()) {

            Address ad = new Address();
            ad.setId(addressRequestDto.getId() != null ? addressRequestDto.getId() : null);
            ad.setStreet(addressRequestDto.getStreet());
            ad.setAddressType(addressRequestDto.getAddressType());
            ad.setCity(addressRequestDto.getCity());
            ad.setCountry(addressRequestDto.getCountry());
            ad.setId(addressRequestDto.getId());
            ad.setPincode(addressRequestDto.getPincode());
            ad.setEmpId(addressRequest.getEmpId());

            listToSave.add(ad);

        }

        return listToSave;
    }

}