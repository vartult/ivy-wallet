package com.ivy.wallet.logic.bankintegrations

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.ivy.wallet.base.toLowerCaseLocal
import com.ivy.wallet.model.IvyCurrency
import com.ivy.wallet.model.bankintegrations.SEAccount
import com.ivy.wallet.model.bankintegrations.SETransaction
import com.ivy.wallet.model.entity.Account
import com.ivy.wallet.persistence.dao.AccountDao
import com.ivy.wallet.ui.theme.components.IVY_COLOR_PICKER_COLORS_FREE
import java.util.*

class SaltEdgeAccountMapper(
    private val accountDao: AccountDao
) {

    fun mapAccount(
        seAccounts: List<SEAccount>,
        seTransaction: SETransaction
    ): UUID? {
        val existingAccount = accountDao.findBySeAccountId(
            seAccountId = seTransaction.account_id
        )

        val account = if (existingAccount == null) {
            //create account
            val seAccount = seAccounts.find { it.id == seTransaction.account_id } ?: return null

            val account = Account(
                seAccountId = seAccount.id,

                name = seAccount.name,
                color = mapColor(seAccount).toArgb(),
                icon = mapIcon(seAccount),

                currency = IvyCurrency.fromCode(seAccount.currency_code)?.code,
                orderNum = accountDao.findMaxOrderNum(),

                isSynced = false,
                isDeleted = false
            )

            accountDao.save(account)

            account
        } else existingAccount

        return account.id
    }

    private fun mapColor(seAccount: SEAccount): Color {
        //TODO: Create better mapping
        return IVY_COLOR_PICKER_COLORS_FREE.shuffled().first()
    }

    private fun mapIcon(seAccount: SEAccount): String? {
        //TODO: Create better mapping
        return when {
            seAccount.name.toLowerCaseLocal().contains("revolut") -> {
                "revolut"
            }
            else -> null
        }
    }
}