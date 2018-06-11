package com.etermax.spacehorse.core.inapps.market.editor;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.etermax.spacehorse.configuration.EnviromentType;
import com.etermax.spacehorse.core.inapps.domain.Market;
import com.etermax.spacehorse.core.inapps.domain.market.editor.EditorMarket;

public class EditorMarketTest {

	private Market market;

	@Test
	public void editorMarketRejectsPurchasesInProduction() {
		givenAnEditorMarket();
		thenTheMarketRejectsPurchasesInProduction();
	}

	@Test
	public void editorMarketAcceptsPurchasesInDevAndStaging() {
		givenAnEditorMarket();
		thenTheMarketAcceptsPurchasesInDev();
		thenTheMarketAcceptsPurchasesInStaging();
	}

	private void thenTheMarketAcceptsPurchasesInStaging() {
		thenTheMarketAcceptPurchasesIn(EnviromentType.STAGING);
	}

	private void thenTheMarketAcceptsPurchasesInDev() {
		thenTheMarketAcceptPurchasesIn(EnviromentType.DEVELOPMENT);
	}

	private void thenTheMarketRejectsPurchasesInProduction() {
		assertThat(market.canBeUsedIn(EnviromentType.PRODUCTION), equalTo(false));
	}

	private void thenTheMarketAcceptPurchasesIn(EnviromentType env) {
		assertThat(market.canBeUsedIn(env), equalTo(true));
	}

	private void givenAnEditorMarket() {
		market = new EditorMarket();
	}

}