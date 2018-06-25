package TestBoard;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.qainfotech.tap.training.snl.api.Board;
import com.qainfotech.tap.training.snl.api.BoardModel;
import com.qainfotech.tap.training.snl.api.GameInProgressException;
import com.qainfotech.tap.training.snl.api.InvalidTurnException;
import com.qainfotech.tap.training.snl.api.MaxPlayersReachedExeption;
import com.qainfotech.tap.training.snl.api.NoUserWithSuchUUIDException;
import com.qainfotech.tap.training.snl.api.PlayerExistsException;

import junit.framework.Assert;

public class accessBoardFunction {
	Board b;
	JSONArray data;
	int i;
	JSONObject response;
	List<String> name=new ArrayList<String>();
	List<UUID> UUid=new ArrayList<UUID>();
	public accessBoardFunction() throws FileNotFoundException, UnsupportedEncodingException, IOException {
		b=new Board();
	}
	public void runRegisterFunction(String nameOfPlayer) throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException {
		
		data=b.registerPlayer(nameOfPlayer);
			//System.out.println((String) data.getJSONObject(i).get("uuid")+"  "+data.getJSONObject(i).get("name"));
			if(name.indexOf(nameOfPlayer)==-1) {
				this.name.add(nameOfPlayer);
				UUid.add(UUID.fromString((String) data.getJSONObject(i).get("uuid"))); 			 
			}
			i++;
	}
	public void addPlayersForFourTime() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException {
		for(int i=1;i<=4;i++) {
			runRegisterFunction("Player"+i);
		}
	}
	public void whenPlayerRolldiceWithoutHavingItsTurn() throws InvalidTurnException, IOException {
		response=b.rollDice(UUid.get(0));
		checkIfPlayerHasMovedToCorrectPosition();
		response=b.rollDice(UUid.get(1));
		checkIfPlayerHasMovedToCorrectPosition();
		response=b.rollDice(UUid.get(2));
		checkIfPlayerHasMovedToCorrectPosition();
		try{
			b.rollDice(UUid.get(0));
		}catch (Exception e) {
			assertThat(e).hasMessage("Player '"+UUid.get(0).toString()+"' does not have the turn");
		}
	}
	public void whenInvalidPlayerRolldiceWithoutHavingItsTurn() throws FileNotFoundException, UnsupportedEncodingException, InvalidTurnException {
		UUID uuid=UUID.randomUUID();
		try{
			b.rollDice(uuid);
		}catch (Exception e) {
			assertThat(e).hasMessage("Player '"+uuid.toString()+"' does not have the turn");
		}
	}
	public void deletePlayer(UUID uuid) throws NoUserWithSuchUUIDException, JSONException, IOException {
		b.deletePlayer(uuid);
	}
	public boolean checkforPlayerAddition() {
		 for (int i = 0; i < data.length(); i++) {
			  if(!data.getJSONObject(i).has(name.get(0))) {
				 return true;
			 }
		 }
		 return false;
	}
	public boolean checkforPlayerDeletion() {
		 for (int i = 0; i < data.length(); i++) { 
			 if(data.getJSONObject(i).has(name.get(0))) {
				 return false;
			 }
		 }
		 return true;
	}
	public void checkForDeletionOfPlayerIfGivenUuidDoesNotExist() throws FileNotFoundException, UnsupportedEncodingException, NoUserWithSuchUUIDException {
		UUID uuid=UUID.randomUUID();
		try{
			deletePlayer(uuid);
		}catch(Exception e) {
			assertThat(e).hasMessage("No Player with uuid '"+uuid+"' on board");
		}
		
	}
	public void checkForDeletionOfPlayerIfGivenUuidExist() throws NoUserWithSuchUUIDException, JSONException, IOException {
		deletePlayer(UUid.get(0));
		Assert.assertTrue(checkforPlayerDeletion());
	}
	public boolean checkIfPlayerHasMovedToCorrectPosition() throws InvalidTurnException, IOException {
		JSONObject data=BoardModel.data(b.getUUID());
		Integer turn = data.getInt("turn");
		JSONObject player = data.getJSONArray("players").getJSONObject(turn);
		Integer currentPosition = player.getInt("position");
		Integer nPosition = player.getInt("position");
		Integer newPosition=b.getData().getJSONArray("steps").getJSONObject(currentPosition+response.getInt("dice")).getInt("target");
		if(nPosition==newPosition) {
			return true;
		}
		else return false;
	}
	public void ifPlayerHasMovedToCorrectPosition() throws InvalidTurnException, IOException {
		Assert.assertTrue(checkIfPlayerHasMovedToCorrectPosition());
	}
	 public void whetherPlayerIsAddedOrNot() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException {
		  runRegisterFunction("Anoop Kumar");
		  Assert.assertTrue(checkforPlayerAddition());
		  
	  }
	 public void whenGameIsInProgress() throws FileNotFoundException, UnsupportedEncodingException, InvalidTurnException {
		 b.rollDice(UUid.get(0));
		 try {
			 runRegisterFunction("Anoop");
		 }
		 catch(Exception e){
			 assertThat(e).hasMessage("New player cannot join since the game has started");
		 }
	 }
	 public void whenPlayerNameIsSame() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException {
		  runRegisterFunction("Anoop Kumar");
		  try {
			  runRegisterFunction("Anoop Kumar");
		  }
		  catch(Exception e) {
			  assertThat(e).hasMessage("Player 'Anoop Kumar' already exists on board");
		  }  
	  }
	 public void forNumberOfMaxPlayer() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException {
		  addPlayersForFourTime();
		  try {
			  runRegisterFunction("Anoop Kumar");
		  }
		  catch(Exception e) {
			  assertThat(e).hasMessage("The board already has maximum allowed Player: "+4);
		  }  
	  }
}
