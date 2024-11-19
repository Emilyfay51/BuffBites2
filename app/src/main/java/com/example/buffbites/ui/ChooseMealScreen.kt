package com.example.buffbites.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.buffbites.BuffBitesScreen
import com.example.buffbites.R
import com.example.buffbites.cancelOrderAndNavigateToStart
import com.example.buffbites.data.Datasource
import com.example.buffbites.model.MenuItem
import com.example.buffbites.ui.theme.BuffBitesTheme
import java.text.NumberFormat

@Composable
fun ChooseMenuScreen(
    options: List<MenuItem>,
    onSelectionChanged: (MenuItem) -> Unit,
    modifier: Modifier = Modifier,
    onNextButtonClicked: () -> Unit,
    onCancelButtonClicked: () -> Unit,
) {
    var selectedItemName by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
    ) {
        options.forEach { item ->
            val onClick = {
                selectedItemName = item.name
                onSelectionChanged(item)
            }
            MenuItemRow(
                item = item,
                selectedItemName = selectedItemName,
                onClick = onClick,
                modifier = Modifier.selectable(
                    selected = selectedItemName == item.name,
                    onClick = onClick
                )
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        MenuScreenButtonGroup(
            selectedItemName = selectedItemName,
            onCancelButtonClicked = onCancelButtonClicked,
            onNextButtonClicked = onNextButtonClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Composable
fun MenuItemRow(
    item: MenuItem,
    selectedItemName: String,
    onClick: () -> Unit,

    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selectedItemName == item.name,
            onClick = onClick
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = NumberFormat.getCurrencyInstance().format(item.price),
                style = MaterialTheme.typography.bodyMedium
            )
            HorizontalDivider(
                modifier = Modifier.padding(bottom = 16.dp),
                thickness = 1.dp
            )
        }
    }
}

@Composable
fun MenuScreenButtonGroup(
    selectedItemName: String,
    onCancelButtonClicked: () -> Unit,
    onNextButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Bottom
    ){
        OutlinedButton(modifier = Modifier.weight(1f), onClick = onCancelButtonClicked) {
            Text(stringResource(R.string.cancel))
        }
        Button(
            modifier = Modifier.weight(1f),
            // the button is enabled when the user makes a selection
            enabled = selectedItemName.isNotEmpty(),
            onClick = onNextButtonClicked
        ) {
            Text(stringResource(R.string.next))
        }
    }
}

@Preview
@Composable
fun MenuScreenPreview() {
    val navController = rememberNavController()
    val viewModel: OrderViewModel<Any?> = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    BuffBitesTheme {
        ChooseMenuScreen(
            options = Datasource.restaurants[0].menuItems,
            onSelectionChanged = {},
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            onNextButtonClicked = { navController.navigate(BuffBitesScreen.Delivery.name) },
            onCancelButtonClicked = {
                cancelOrderAndNavigateToStart(viewModel, navController)
            }
        )
    }
}