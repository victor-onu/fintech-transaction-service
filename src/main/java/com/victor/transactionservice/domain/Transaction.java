package com.victor.transactionservice.domain;

import com.victor.transactionservice.domain.enumeration.TransactionStatus;
import com.victor.transactionservice.domain.enumeration.TransactionType;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Transaction.
 */
@Entity
@Table(name = "transaction")
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TransactionType type;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "commission_amount")
    private Double commissionAmount;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "payment_reference", nullable = false)
    private String paymentReference;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TransactionStatus status;

    @Column(name = "source_account_id")
    private String sourceAccountId;

    @Column(name = "destination_account_id")
    private String destinationAccountId;

    @Column(name = "response_message")
    private String responseMessage;

    @NotNull
    @Column(name = "account_owner_id", nullable = false)
    private String accountOwnerId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Transaction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransactionType getType() {
        return this.type;
    }

    public Transaction type(TransactionType type) {
        this.setType(type);
        return this;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Double getAmount() {
        return this.amount;
    }

    public Transaction amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getCommissionAmount() {
        return this.commissionAmount;
    }

    public Transaction commissionAmount(Double commissionAmount) {
        this.setCommissionAmount(commissionAmount);
        return this;
    }

    public void setCommissionAmount(Double commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public String getDescription() {
        return this.description;
    }

    public Transaction description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPaymentReference() {
        return this.paymentReference;
    }

    public Transaction paymentReference(String paymentReference) {
        this.setPaymentReference(paymentReference);
        return this;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public TransactionStatus getStatus() {
        return this.status;
    }

    public Transaction status(TransactionStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getSourceAccountId() {
        return this.sourceAccountId;
    }

    public Transaction sourceAccountId(String sourceAccountId) {
        this.setSourceAccountId(sourceAccountId);
        return this;
    }

    public void setSourceAccountId(String sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
    }

    public String getDestinationAccountId() {
        return this.destinationAccountId;
    }

    public Transaction destinationAccountId(String destinationAccountId) {
        this.setDestinationAccountId(destinationAccountId);
        return this;
    }

    public void setDestinationAccountId(String destinationAccountId) {
        this.destinationAccountId = destinationAccountId;
    }

    public String getResponseMessage() {
        return this.responseMessage;
    }

    public Transaction responseMessage(String responseMessage) {
        this.setResponseMessage(responseMessage);
        return this;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getAccountOwnerId() {
        return this.accountOwnerId;
    }

    public Transaction accountOwnerId(String accountOwnerId) {
        this.setAccountOwnerId(accountOwnerId);
        return this;
    }

    public void setAccountOwnerId(String accountOwnerId) {
        this.accountOwnerId = accountOwnerId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transaction)) {
            return false;
        }
        return id != null && id.equals(((Transaction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transaction{" +
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
