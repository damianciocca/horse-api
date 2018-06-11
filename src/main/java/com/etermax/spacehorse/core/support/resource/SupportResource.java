package com.etermax.spacehorse.core.support.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.etermax.spacehorse.core.login.action.LoginSupportUserAction;
import com.etermax.spacehorse.core.player.action.PlayerAction;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.inventory.Inventory;
import com.etermax.spacehorse.core.support.action.DepositAction;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/v1/support")
@Api(value = "/support", tags = "Support")
public class SupportResource {

	private final LoginSupportUserAction loginAction;
	private final PlayerAction playerAction;
	private final DepositAction depositAction;

	public SupportResource(LoginSupportUserAction loginAction, PlayerAction playerAction, DepositAction depositAction) {
		this.loginAction = loginAction;
		this.playerAction = playerAction;
		this.depositAction = depositAction;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/depositFunds")
	@ApiOperation("Deposit any capital to an specific player id. Only for SUPPORT role.")
	public Response depositFunds(DepositFundsRequest request) {
		String supportLoginId = request.getSupportLoginId();
		try {
			loginAction.login(supportLoginId, request.getSupportPassword());
		} catch (Exception e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

		Player player = playerAction.findByLoginIdOrFail(request.getPlayerId());
		depositAction.deposit(player, getGemsToDeposit(request), getGoldToDeposit(request));

		DepositFundsResponse response = createDepositFundsResponse(request, player);
		return Response.ok(response).build();
	}

	private DepositFundsResponse createDepositFundsResponse(DepositFundsRequest request, Player player) {
		Inventory inventory = player.getInventory();
		return new DepositFundsResponse(request.getPlayerId(), player.getName(), inventory.getGold().getAmount(), inventory.getGems().getAmount());
	}

	private int getGoldToDeposit(DepositFundsRequest request) {
		return request.getGoldAmount();
	}

	private int getGemsToDeposit(DepositFundsRequest request) {
		return request.getGemsAmount();
	}
}
