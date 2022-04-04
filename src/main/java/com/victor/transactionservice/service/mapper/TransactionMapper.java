package com.victor.transactionservice.service.mapper;

import com.victor.transactionservice.domain.Transaction;
import com.victor.transactionservice.service.dto.TransactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Transaction} and its DTO {@link TransactionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TransactionMapper extends EntityMapper<TransactionDTO, Transaction> {}
