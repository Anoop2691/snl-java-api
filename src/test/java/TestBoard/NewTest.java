package TestBoard;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.qainfotech.tap.training.snl.api.GameInProgressException;
import com.qainfotech.tap.training.snl.api.InvalidTurnException;
import com.qainfotech.tap.training.snl.api.MaxPlayersReachedExeption;
import com.qainfotech.tap.training.snl.api.NoUserWithSuchUUIDException;
import com.qainfotech.tap.training.snl.api.PlayerExistsException;

import junit.framework.Assert;

public class NewTest {
	accessBoardFunction ObjectForOnePlayer;
	accessBoardFunction ObjectForFourPlayer;
	accessBoardFunction ObjectForOnePlayerForAnotherPurposes;
  @BeforeClass
  void CreateObjectForaccessBoardFunction() throws FileNotFoundException, UnsupportedEncodingException, IOException {
	  ObjectForOnePlayer=new accessBoardFunction();
	  ObjectForFourPlayer=new accessBoardFunction();
	  ObjectForOnePlayerForAnotherPurposes=new accessBoardFunction();
  }
  
  @Test
  public void checkWhetherPlayerIsAddedOrNot() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException {
	  ObjectForOnePlayerForAnotherPurposes.whetherPlayerIsAddedOrNot();	  
  }
  @Test(dependsOnMethods="checkWhetherPlayerIsAddedOrNot")
  public void testWhenPlayerNameIsSame() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException {
	  ObjectForOnePlayer.whenPlayerNameIsSame();
  }
  @Test
  public void testForNumberOfMaxPlayer() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException {
	  ObjectForFourPlayer.forNumberOfMaxPlayer();
  }
  @Test(dependsOnMethods="checkWhetherPlayerIsAddedOrNot")
  public void testWhenGameIsInProgress() throws FileNotFoundException, UnsupportedEncodingException, InvalidTurnException {
	  ObjectForOnePlayerForAnotherPurposes.whenGameIsInProgress();
  }
  @Test
  public void deletePlayerUUIDNotExist() throws FileNotFoundException, UnsupportedEncodingException, NoUserWithSuchUUIDException {
	  ObjectForFourPlayer.checkForDeletionOfPlayerIfGivenUuidDoesNotExist();
  }
  @Test(dependsOnMethods="testForNumberOfMaxPlayer")
  public void whenPlayerRolldiceWithoutHavingItsTurn() throws InvalidTurnException, IOException {
	  ObjectForFourPlayer.whenPlayerRolldiceWithoutHavingItsTurn();
  }
  @Test(dependsOnMethods="whenPlayerRolldiceWithoutHavingItsTurn")
  public void whenInvalidPlayerRolldiceWithoutHavingItsTurn() throws FileNotFoundException, UnsupportedEncodingException, InvalidTurnException {
	  ObjectForFourPlayer.whenInvalidPlayerRolldiceWithoutHavingItsTurn();
  }
  @Test(dependsOnMethods="testWhenGameIsInProgress")
  public void deletePlayerUUIDExist() throws NoUserWithSuchUUIDException, JSONException, IOException {
	  ObjectForOnePlayerForAnotherPurposes.checkForDeletionOfPlayerIfGivenUuidExist();
  }
  
}
