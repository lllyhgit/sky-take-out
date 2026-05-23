package com.sky.controller.user;

import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Tag(name = "C端地址簿")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    @PostMapping
    @Operation(summary = "新增地址")
    public Result<?> save(@RequestBody AddressBook addressBook) {
        addressBookService.save(addressBook);
        return Result.success();
    }

    @PutMapping
    @Operation(summary = "编辑地址")
    public Result<?> update(@RequestBody AddressBook addressBook) {
        addressBookService.update(addressBook);
        return Result.success();
    }

    @DeleteMapping
    @Operation(summary = "删除地址")
    public Result<?> deleteById(Long id) {
        addressBookService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询地址详情")
    public Result<AddressBook> getById(@PathVariable Long id) {
        return Result.success(addressBookService.getById(id));
    }

    @GetMapping("/list")
    @Operation(summary = "查询地址列表")
    public Result<List<AddressBook>> list() {
        return Result.success(addressBookService.list());
    }

    @PutMapping("/default")
    @Operation(summary = "设为默认地址")
    public Result<?> setDefault(@RequestBody AddressBook addressBook) {
        addressBookService.setDefault(addressBook.getId());
        return Result.success();
    }
}
