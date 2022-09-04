package org.isd.bankgrpcservice.grpc.service;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.isd.bankgrpcservice.entities.Account;
import org.isd.bankgrpcservice.entities.Currency;
import org.isd.bankgrpcservice.grpc.stub.Bank;
import org.isd.bankgrpcservice.grpc.stub.BankServiceGrpc;
import org.isd.bankgrpcservice.mappers.BankAccountMapperImpl;
import org.isd.bankgrpcservice.repository.AccountRepository;
import org.isd.bankgrpcservice.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class BankGrpcServiceImpl extends BankServiceGrpc.BankServiceImplBase {
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private BankAccountMapperImpl accountMapper;

    @Override
    public void getBankAccount(Bank.GetBankAccountRequest request, StreamObserver<Bank.GetBankAccountResponse> responseObserver) {
        String accountId=request.getAccountId();
        Account account = accountRepository.findById(accountId).orElse(null);
        if(account!=null){
            Bank.BankAccount bankAccount=accountMapper.fromBankCount(account);
            Bank.GetBankAccountResponse accountResponse= Bank.GetBankAccountResponse.newBuilder()
                    .setBankAccount(bankAccount)
                    .build();
            responseObserver.onNext(accountResponse);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getListAccounts(Bank.GetListAccountsRequest request, StreamObserver<Bank.GetListAccountsResponse> responseObserver) {
        List<Account> accountList=accountRepository.findAll();
        List<Bank.BankAccount> grpcAccountList = accountList.stream().map(account -> accountMapper.fromBankCount(account)).collect(Collectors.toList());
        Bank.GetListAccountsResponse listAccountsResponse= Bank.GetListAccountsResponse.newBuilder()
                .addAllBankAccount(grpcAccountList)
                .build();
        responseObserver.onNext(listAccountsResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void convertCurrency(Bank.ConvertCurrencyRequest request, StreamObserver<Bank.ConvertCurrencyResponse> responseObserver) {
        String from=request.getCurrencyFrom();
        String to=request.getCurrencyTo();
        double amount=request.getAmount();
        Currency currencyFrom=currencyRepository.findByName(from);
        Currency currencyTo=currencyRepository.findByName(to);
        double result=amount*currencyFrom.getPrice()/currencyTo.getPrice();
        Bank.ConvertCurrencyResponse convertCurrencyResponse= Bank.ConvertCurrencyResponse.newBuilder()
                .setCurrencyFrom(from)
                .setCurrencyTo(to)
                .setAmount(amount)
                .setConversionResult(result)
                .build();
        responseObserver.onNext(convertCurrencyResponse);
        responseObserver.onCompleted();
    }
}
