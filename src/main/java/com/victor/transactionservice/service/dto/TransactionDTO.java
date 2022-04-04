package com.victor.transactionservice.service.dto;

import com.victor.transactionservice.domain.enumeration.TransactionStatus;
import com.victor.transactionservice.domain.enumeration.TransactionType;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.victor.transactionservice.domain.Transaction} entity.
 */
public class TransactionDTO implements Serializable {

    private Long id;

    private TransactionType type;

    @NotNull
    private Double amount;

    private Double commissionAmount;

    private String description;

    @NotNull
    private String paymentReference;

    private TransactionStatus status;

    private String sourceAccountId;

    private String destinationAccountId;

    private String responseMessage;

    @NotNull
    private String accountOwnerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(Double commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getSourceAccountId() {
        return sourceAccountId;
    }

    public void setSourceAccountId(String sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
    }

    public String getDestinationAccountId() {
        return destinationAccountId;
    }

    public void setDestinationAccountId(String destinationAccountId) {
        this.destinationAccountId = destinationAccountId;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getAccountOwnerId() {
        return accountOwnerId;
    }

    public void setAccountOwnerId(String accountOwnerId) {
        this.accountOwnerId = accountOwnerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionDTO)) {
            return false;
        }

        TransactionDTO transactionDTO = (TransactionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transactionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", amount=" + getAmount() +
            ", commissionAmount=" + getCommissionAmount() +
            ", description='" + getDescription() + "'" +
            ", paymentReference='" + getPaymentReference() + "'" +
            ", status='" + getStatus() + "'" +
            ", sourceAccountId='" + getSourceAccountId() + "'" +
            ", destinationAccountId='" + getDestinationAccountId() + "'" +
            ", responseMessage='" + getResponseMessage() + "'" +
            ", accountOwnerId='" + getAccountOwnerId() + "'" +
            "}";
    }
}
