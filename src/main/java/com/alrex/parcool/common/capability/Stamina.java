package com.alrex.parcool.common.capability;

import com.alrex.parcool.api.Attributes;
import com.alrex.parcool.api.Effects;
import com.alrex.parcool.config.ParCoolConfig;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nullable;

public class Stamina implements IStamina {
	public Stamina(@Nullable PlayerEntity player) {
		this.player = player;
		if (player != null && player.isLocalPlayer()) {
            set(Integer.MAX_VALUE);
		}
	}

	public Stamina() {
		this.player = null;
	}

	@Nullable
	private final PlayerEntity player;

	private int stamina = 0;
	private int staminaOld = 0;
	private boolean exhausted = false;

	@Override
	public int getActualMaxStamina() {
        if (player == null) return 1;
		Parkourability parkourability = Parkourability.get(player);
        if (parkourability == null) return 1;
        ModifiableAttributeInstance attr = player.getAttribute(Attributes.MAX_STAMINA.get());
        if (attr == null) return 1;
        return Math.min((int) attr.getValue(), parkourability.getActionInfo().getMaxStaminaLimit());
	}

	@Override
	public int get() {
		return stamina;
	}

	@Override
	public int getOldValue() {
		return staminaOld;
	}

	@Override
	public void consume(int value) {
		if (player == null) return;
		Parkourability parkourability = Parkourability.get(player);
		if (parkourability == null) return;
		if (exhausted
				|| parkourability.getActionInfo().isStaminaInfinite(player.isSpectator() || player.isCreative())
                || player.hasEffect(Effects.INEXHAUSTIBLE.get())
		) return;
		if (ParCoolConfig.Client.Booleans.UseHungerBarInstead.get()) {
			player.causeFoodExhaustion(value / 1000f);
			return;
		}
		recoverCoolTime = 30;
		set(stamina - value);
		if (stamina == 0) {
			exhausted = true;
		}
	}

	@Override
	public void recover(int value) {
		set(stamina + value);
		if (stamina == getActualMaxStamina()) {
			exhausted = false;
		}
	}

	@Override
	public boolean isExhausted() {
		return exhausted;
	}

	@Override
	public void setExhaustion(boolean value) {
		exhausted = value;
	}

	private int recoverCoolTime = 0;

	@Override
	public void tick() {
		staminaOld = stamina;
		if (recoverCoolTime > 0) recoverCoolTime--;
		if (recoverCoolTime <= 0) {
			if (player == null) return;
			Parkourability parkourability = Parkourability.get(player);
			if (parkourability == null) return;
            ModifiableAttributeInstance attr = player.getAttribute(Attributes.STAMINA_RECOVERY.get());
            if (attr == null) return;
            recover(Math.min((int) attr.getValue(), parkourability.getActionInfo().getStaminaRecoveryLimit()));
		}
	}

	@Override
	public void set(int value) {
		stamina = Math.min(value, getActualMaxStamina());
		if (stamina <= 0) {
			stamina = 0;
		}
	}
}
