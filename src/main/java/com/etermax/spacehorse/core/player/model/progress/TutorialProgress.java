package com.etermax.spacehorse.core.player.model.progress;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.etermax.spacehorse.core.player.action.TutorialException;

import java.util.ArrayList;
import java.util.List;

@DynamoDBDocument
public class TutorialProgress {

	@DynamoDBAttribute(attributeName = "finishedTutorials")
	private List<String> finishedTutorials;

	@DynamoDBAttribute(attributeName = "activeTutorial")
	private String activeTutorial;

	public TutorialProgress() {
		this.finishedTutorials = new ArrayList<>();
	}

	public static TutorialProgress buildNewTutorialProgress() {
		return new TutorialProgress();
	}

	public void validateTutorialId(String tutorialId) {
		if (finishedTutorials.contains(tutorialId)) {
			throw new TutorialException("Tutorial already finished.");
		}
		if ((activeTutorial != null) && (!activeTutorial.isEmpty()) && (!activeTutorial.equals(tutorialId))) {
			throw new TutorialException("Another tutorial is already active.");
		}
	}

	public Boolean isTutorialIdActive(String tutorialId) {
		return hasActiveTutorial() && activeTutorial.equals(tutorialId);
	}

	public boolean hasActiveTutorial() {
		return (activeTutorial != null) && (!activeTutorial.isEmpty());
	}

	public void finishActiveTutorial() {
		if (hasActiveTutorial()) {
			addFinishedTutorial(activeTutorial);
		}
		activeTutorial = null;
	}

	public List<String> getFinishedTutorials() {
		return finishedTutorials;
	}

	public void setFinishedTutorials(List<String> finishedTutorials) {
		this.finishedTutorials = finishedTutorials;
	}

	public String getActiveTutorial() {
		return activeTutorial;
	}

	public void setActiveTutorial(String activeTutorial) {
		this.activeTutorial = activeTutorial;
	}

	public void addFinishedTutorial(String tutorialId) {
		if (!isTutorialIdFinished(tutorialId)) {
			finishedTutorials.add(tutorialId);
		}
	}

	public boolean isTutorialIdFinished(String tutorialId) {
		return finishedTutorials.stream().anyMatch(finishedTutorialId -> finishedTutorialId.equals(tutorialId));
	}

	public void removeFinishedTutorialId(String tutorialId) {
		finishedTutorials.remove(tutorialId);
	}
}
