package com.cronoteSys.model.vo;

public enum StatusEnum {

	NOT_STARTED("N�o iniciado", "#D3D3D3"), NORMAL_IN_PROGRESS("Em progresso", "#1232C6"),
	NORMAL_PAUSED("Pausado", "#FFC61E"), NORMAL_FINALIZED("Finalizado", "#7ED957"),
	BROKEN_IN_PROGRESS("Em progresso", "#FF5757"), BROKEN_PAUSED("Pausado", "#FF5757"),
	BROKEN_FINALIZED("Finalizado", "#FF5757");

	private String description;
	private String hexColor;

	public static StatusEnum getBroken(StatusEnum stats) {
		switch (stats) {
		case NORMAL_IN_PROGRESS:
			return BROKEN_IN_PROGRESS;
		case NORMAL_PAUSED:
			return BROKEN_PAUSED;
		case NORMAL_FINALIZED:
			return BROKEN_FINALIZED;
		default:
			return stats;
		}
	}

	public static boolean itsFinalized(StatusEnum stats) {
		if (stats == BROKEN_FINALIZED || stats == NORMAL_FINALIZED)
			return true;
		return false;
	}

	public static boolean itsPaused(StatusEnum stats) {
		if (stats == BROKEN_PAUSED || stats == NORMAL_PAUSED)
			return true;
		return false;
	}

	public static boolean inProgress(StatusEnum stats) {
		if (stats == BROKEN_IN_PROGRESS || stats == NORMAL_IN_PROGRESS)
			return true;
		return false;
	}

	private StatusEnum(String description, String hexColor) {
		this.description = description;
		this.hexColor = hexColor;
	}

	public String getDescription() {
		return description;
	}

	public String getHexColor() {
		return hexColor;
	}
}
