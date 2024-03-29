package com.sky.service.impl;

import com.sky.context.ThreadLocalUtil;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {


    @Autowired
    private AddressBookMapper addressBookMapper;

    @Override
    public List<AddressBook> getList() {

        //QueryWrapper<AddressBook> queryWrapper =new QueryWrapper<>();
//        if(ThreadLocalUtil.getCurrentId()!=null){
//            queryWrapper.eq("user_id",ThreadLocalUtil.getCurrentId());
//        }
//        queryWrapper.orderByAsc("user_id");


        Long userId = ThreadLocalUtil.getCurrentId();
        //List<AddressBook> addressBookList = addressBookMapper.selectList(queryWrapper);

        return addressBookMapper.getByUserId(userId);
        // addressBookList;
    }


    @Transactional
    @Override
    public void save(AddressBook addressBook) {
        addressBook.setUserId(ThreadLocalUtil.getCurrentId());
        addressBook.setIsDefault(0);
        addressBookMapper.insert(addressBook);
    }

    @Override
    public AddressBook getDefault() {
        List<AddressBook> addressBookList = addressBookMapper.getByUserId(ThreadLocalUtil.getCurrentId());
        for (AddressBook addressBook : addressBookList) {
            if (addressBook.getIsDefault() == 1) {
                return addressBook;
            }

        }

        return null;


    }


    @Override
    public AddressBook getById(Long id) {
        return addressBookMapper.selectById(id);
    }



    @Transactional
    @Override
    public void update(AddressBook addressBook) {
        addressBookMapper.updateById(addressBook);

    }


    @Transactional
    @Override
    public void deleteById(Long id) {
        addressBookMapper.deleteById(id);
    }


    @Transactional
    @Override
    public void updateDefault(AddressBook addressBook) {
        List<AddressBook> addressBookList = addressBookMapper.getByUserId(ThreadLocalUtil.getCurrentId());
        addressBookList.forEach(addressBook1 -> {addressBook1.setIsDefault(0);addressBookMapper.updateById(addressBook1);});
       // AddressBook addressBook = addressBookMapper.selectById(id);

        addressBook=addressBookMapper.selectById(addressBook.getId());
        addressBook.setIsDefault(1);
        addressBookMapper.updateById(addressBook);
    }
}
