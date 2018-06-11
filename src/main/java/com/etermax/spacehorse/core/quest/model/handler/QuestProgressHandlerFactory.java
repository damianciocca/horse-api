package com.etermax.spacehorse.core.quest.model.handler;

public interface QuestProgressHandlerFactory {
	QuestProgressHandler build(String questType);
}
