package com.victor.transactionservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.victor.transactionservice.IntegrationTest;
import com.victor.transactionservice.domain.Transaction;
import com.victor.transactionservice.domain.enumeration.TransactionStatus;
import com.victor.transactionservice.domain.enumeration.TransactionType;
import com.victor.transactionservice.repository.TransactionRepository;
import com.victor.transactionservice.service.dto.TransactionDTO;
import com.victor.transactionservice.service.mapper.TransactionMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TransactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransactionResourceIT {

    private static final TransactionType DEFAULT_TYPE = TransactionType.DEPOSIT;
    private static final TransactionType UPDATED_TYPE = TransactionType.WITHDRAWAL;

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final Double DEFAULT_COMMISSION_AMOUNT = 1D;
    private static final Double UPDATED_COMMISSION_AMOUNT = 2D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_REFERENCE = "BBBBBBBBBB";

    private static final TransactionStatus DEFAULT_STATUS = TransactionStatus.PENDING;
    private static final TransactionStatus UPDATED_STATUS = TransactionStatus.FAILED;

    private static final String DEFAULT_SOURCE_ACCOUNT_ID = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_ACCOUNT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DESTINATION_ACCOUNT_ID = "AAAAAAAAAA";
    private static final String UPDATED_DESTINATION_ACCOUNT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_RESPONSE_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSE_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_OWNER_ID = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_OWNER_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransactionMockMvc;

    private Transaction transaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transaction createEntity(EntityManager em) {
        Transaction transaction = new Transaction()
            .type(DEFAULT_TYPE)
            .amount(DEFAULT_AMOUNT)
            .commissionAmount(DEFAULT_COMMISSION_AMOUNT)
            .description(DEFAULT_DESCRIPTION)
            .paymentReference(DEFAULT_PAYMENT_REFERENCE)
            .status(DEFAULT_STATUS)
            .sourceAccountId(DEFAULT_SOURCE_ACCOUNT_ID)
            .destinationAccountId(DEFAULT_DESTINATION_ACCOUNT_ID)
            .responseMessage(DEFAULT_RESPONSE_MESSAGE)
            .accountOwnerId(DEFAULT_ACCOUNT_OWNER_ID);
        return transaction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transaction createUpdatedEntity(EntityManager em) {
        Transaction transaction = new Transaction()
            .type(UPDATED_TYPE)
            .amount(UPDATED_AMOUNT)
            .commissionAmount(UPDATED_COMMISSION_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .paymentReference(UPDATED_PAYMENT_REFERENCE)
            .status(UPDATED_STATUS)
            .sourceAccountId(UPDATED_SOURCE_ACCOUNT_ID)
            .destinationAccountId(UPDATED_DESTINATION_ACCOUNT_ID)
            .responseMessage(UPDATED_RESPONSE_MESSAGE)
            .accountOwnerId(UPDATED_ACCOUNT_OWNER_ID);
        return transaction;
    }

    @BeforeEach
    public void initTest() {
        transaction = createEntity(em);
    }

    @Test
    @Transactional
    void createTransaction() throws Exception {
        int databaseSizeBeforeCreate = transactionRepository.findAll().size();
        // Create the Transaction
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction);
        restTransactionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeCreate + 1);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTransaction.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testTransaction.getCommissionAmount()).isEqualTo(DEFAULT_COMMISSION_AMOUNT);
        assertThat(testTransaction.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTransaction.getPaymentReference()).isEqualTo(DEFAULT_PAYMENT_REFERENCE);
        assertThat(testTransaction.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTransaction.getSourceAccountId()).isEqualTo(DEFAULT_SOURCE_ACCOUNT_ID);
        assertThat(testTransaction.getDestinationAccountId()).isEqualTo(DEFAULT_DESTINATION_ACCOUNT_ID);
        assertThat(testTransaction.getResponseMessage()).isEqualTo(DEFAULT_RESPONSE_MESSAGE);
        assertThat(testTransaction.getAccountOwnerId()).isEqualTo(DEFAULT_ACCOUNT_OWNER_ID);
    }

    @Test
    @Transactional
    void createTransactionWithExistingId() throws Exception {
        // Create the Transaction with an existing ID
        transaction.setId(1L);
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction);

        int databaseSizeBeforeCreate = transactionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setAmount(null);

        // Create the Transaction, which fails.
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction);

        restTransactionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentReferenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setPaymentReference(null);

        // Create the Transaction, which fails.
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction);

        restTransactionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAccountOwnerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setAccountOwnerId(null);

        // Create the Transaction, which fails.
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction);

        restTransactionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransactions() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList
        restTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].commissionAmount").value(hasItem(DEFAULT_COMMISSION_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].paymentReference").value(hasItem(DEFAULT_PAYMENT_REFERENCE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].sourceAccountId").value(hasItem(DEFAULT_SOURCE_ACCOUNT_ID)))
            .andExpect(jsonPath("$.[*].destinationAccountId").value(hasItem(DEFAULT_DESTINATION_ACCOUNT_ID)))
            .andExpect(jsonPath("$.[*].responseMessage").value(hasItem(DEFAULT_RESPONSE_MESSAGE)))
            .andExpect(jsonPath("$.[*].accountOwnerId").value(hasItem(DEFAULT_ACCOUNT_OWNER_ID)));
    }

    @Test
    @Transactional
    void getTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get the transaction
        restTransactionMockMvc
            .perform(get(ENTITY_API_URL_ID, transaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transaction.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.commissionAmount").value(DEFAULT_COMMISSION_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.paymentReference").value(DEFAULT_PAYMENT_REFERENCE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.sourceAccountId").value(DEFAULT_SOURCE_ACCOUNT_ID))
            .andExpect(jsonPath("$.destinationAccountId").value(DEFAULT_DESTINATION_ACCOUNT_ID))
            .andExpect(jsonPath("$.responseMessage").value(DEFAULT_RESPONSE_MESSAGE))
            .andExpect(jsonPath("$.accountOwnerId").value(DEFAULT_ACCOUNT_OWNER_ID));
    }

    @Test
    @Transactional
    void getNonExistingTransaction() throws Exception {
        // Get the transaction
        restTransactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();

        // Update the transaction
        Transaction updatedTransaction = transactionRepository.findById(transaction.getId()).get();
        // Disconnect from session so that the updates on updatedTransaction are not directly saved in db
        em.detach(updatedTransaction);
        updatedTransaction
            .type(UPDATED_TYPE)
            .amount(UPDATED_AMOUNT)
            .commissionAmount(UPDATED_COMMISSION_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .paymentReference(UPDATED_PAYMENT_REFERENCE)
            .status(UPDATED_STATUS)
            .sourceAccountId(UPDATED_SOURCE_ACCOUNT_ID)
            .destinationAccountId(UPDATED_DESTINATION_ACCOUNT_ID)
            .responseMessage(UPDATED_RESPONSE_MESSAGE)
            .accountOwnerId(UPDATED_ACCOUNT_OWNER_ID);
        TransactionDTO transactionDTO = transactionMapper.toDto(updatedTransaction);

        restTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTransaction.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testTransaction.getCommissionAmount()).isEqualTo(UPDATED_COMMISSION_AMOUNT);
        assertThat(testTransaction.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTransaction.getPaymentReference()).isEqualTo(UPDATED_PAYMENT_REFERENCE);
        assertThat(testTransaction.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTransaction.getSourceAccountId()).isEqualTo(UPDATED_SOURCE_ACCOUNT_ID);
        assertThat(testTransaction.getDestinationAccountId()).isEqualTo(UPDATED_DESTINATION_ACCOUNT_ID);
        assertThat(testTransaction.getResponseMessage()).isEqualTo(UPDATED_RESPONSE_MESSAGE);
        assertThat(testTransaction.getAccountOwnerId()).isEqualTo(UPDATED_ACCOUNT_OWNER_ID);
    }

    @Test
    @Transactional
    void putNonExistingTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();
        transaction.setId(count.incrementAndGet());

        // Create the Transaction
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();
        transaction.setId(count.incrementAndGet());

        // Create the Transaction
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();
        transaction.setId(count.incrementAndGet());

        // Create the Transaction
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransactionWithPatch() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();

        // Update the transaction using partial update
        Transaction partialUpdatedTransaction = new Transaction();
        partialUpdatedTransaction.setId(transaction.getId());

        partialUpdatedTransaction.amount(UPDATED_AMOUNT).responseMessage(UPDATED_RESPONSE_MESSAGE).accountOwnerId(UPDATED_ACCOUNT_OWNER_ID);

        restTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransaction))
            )
            .andExpect(status().isOk());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTransaction.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testTransaction.getCommissionAmount()).isEqualTo(DEFAULT_COMMISSION_AMOUNT);
        assertThat(testTransaction.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTransaction.getPaymentReference()).isEqualTo(DEFAULT_PAYMENT_REFERENCE);
        assertThat(testTransaction.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTransaction.getSourceAccountId()).isEqualTo(DEFAULT_SOURCE_ACCOUNT_ID);
        assertThat(testTransaction.getDestinationAccountId()).isEqualTo(DEFAULT_DESTINATION_ACCOUNT_ID);
        assertThat(testTransaction.getResponseMessage()).isEqualTo(UPDATED_RESPONSE_MESSAGE);
        assertThat(testTransaction.getAccountOwnerId()).isEqualTo(UPDATED_ACCOUNT_OWNER_ID);
    }

    @Test
    @Transactional
    void fullUpdateTransactionWithPatch() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();

        // Update the transaction using partial update
        Transaction partialUpdatedTransaction = new Transaction();
        partialUpdatedTransaction.setId(transaction.getId());

        partialUpdatedTransaction
            .type(UPDATED_TYPE)
            .amount(UPDATED_AMOUNT)
            .commissionAmount(UPDATED_COMMISSION_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .paymentReference(UPDATED_PAYMENT_REFERENCE)
            .status(UPDATED_STATUS)
            .sourceAccountId(UPDATED_SOURCE_ACCOUNT_ID)
            .destinationAccountId(UPDATED_DESTINATION_ACCOUNT_ID)
            .responseMessage(UPDATED_RESPONSE_MESSAGE)
            .accountOwnerId(UPDATED_ACCOUNT_OWNER_ID);

        restTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransaction))
            )
            .andExpect(status().isOk());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTransaction.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testTransaction.getCommissionAmount()).isEqualTo(UPDATED_COMMISSION_AMOUNT);
        assertThat(testTransaction.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTransaction.getPaymentReference()).isEqualTo(UPDATED_PAYMENT_REFERENCE);
        assertThat(testTransaction.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTransaction.getSourceAccountId()).isEqualTo(UPDATED_SOURCE_ACCOUNT_ID);
        assertThat(testTransaction.getDestinationAccountId()).isEqualTo(UPDATED_DESTINATION_ACCOUNT_ID);
        assertThat(testTransaction.getResponseMessage()).isEqualTo(UPDATED_RESPONSE_MESSAGE);
        assertThat(testTransaction.getAccountOwnerId()).isEqualTo(UPDATED_ACCOUNT_OWNER_ID);
    }

    @Test
    @Transactional
    void patchNonExistingTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();
        transaction.setId(count.incrementAndGet());

        // Create the Transaction
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transactionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();
        transaction.setId(count.incrementAndGet());

        // Create the Transaction
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();
        transaction.setId(count.incrementAndGet());

        // Create the Transaction
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(transactionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        int databaseSizeBeforeDelete = transactionRepository.findAll().size();

        // Delete the transaction
        restTransactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, transaction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
