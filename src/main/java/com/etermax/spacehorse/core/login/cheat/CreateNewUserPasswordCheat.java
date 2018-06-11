package com.etermax.spacehorse.core.login.cheat;

import com.etermax.spacehorse.core.authenticator.model.PasswordGenerator;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.cheat.model.Cheat;
import com.etermax.spacehorse.core.cheat.resource.response.CheatResponse;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.user.model.User;
import com.etermax.spacehorse.core.user.repository.UserRepository;

public class CreateNewUserPasswordCheat extends Cheat {

	private UserRepository userRepository;

	public CreateNewUserPasswordCheat(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public String getCheatId() {
		return "createNewUserPassword";
	}

	@Override
	public CheatResponse apply(Player player, String[] parameters, Catalog catalog) {
		String userId = getParameterString(parameters, 0);

		User user = userRepository.find(userId);

		if (user != null) {
			String newPassword = PasswordGenerator.generate();
			user.addPassword(newPassword);
			userRepository.update(user);
			return new CheatResponse(newPassword);
		}

		return new CheatResponse("");
	}

}
