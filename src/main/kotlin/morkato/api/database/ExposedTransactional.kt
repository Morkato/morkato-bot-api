package morkato.api.database;

import org.springframework.transaction.annotation.Transactional;

@Transactional(transactionManager = "springTransactionManager")
annotation class ExposedTransactional();