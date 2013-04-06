/**
 * 
 */
package com.techzhiqi.quiz.yizhandaodi;

import com.techzhiqi.quiz.yizhandaodi.quiz.GamePlay;

import android.app.Application;

/**
 * @author zhiqi.lin
 *
 */
public class QuizApplication extends Application{
	private GamePlay currentGame;

	/**
	 * @param currentGame the currentGame to set
	 */
	public void setCurrentGame(GamePlay currentGame) {
		this.currentGame = currentGame;
	}

	/**
	 * @return the currentGame
	 */
	public GamePlay getCurrentGame() {
		return currentGame;
	}
}
