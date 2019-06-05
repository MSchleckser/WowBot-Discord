package wow.bot.mig.hunter.feature.miss.tracker;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import wow.bot.mig.hunter.FireTypes;
import wow.bot.mig.hunter.models.MissLog;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class MissTrackerTest {

	private MissTracker missTracker = new MissTracker();
	private HashMap<String, List<MissLog>> missLogs = new HashMap<>();

	@Mock
	private Message message;

	@Mock
	private User user;

	@Before
	public void setup() {
		missTracker.setMissLogs(missLogs);
	}

	@Test
	public void testLogMiss_userNotInLog() {
		OffsetDateTime offsetDateTime = LocalDateTime.now().atOffset(ZoneOffset.UTC);
		String userName = "user";
		doReturn(user).when(message).getAuthor();
		doReturn(offsetDateTime).when(message).getCreationTime();
		doReturn(userName).when(user).getName();

		assertThat(missTracker.getMissLogs().containsKey(userName), is(false));

		missTracker.logMiss(message, FireTypes.GUNS);

		assertThat(missTracker.getMissLogs().containsKey(userName), is(true));
	}

	@Test
	public void testLogMiss_userInLog() {
		List<MissLog> misses = new ArrayList<>(2);
		misses.add(new MissLog());
		OffsetDateTime offsetDateTime = LocalDateTime.now().atOffset(ZoneOffset.UTC);
		String userName = "user";
		doReturn(user).when(message).getAuthor();
		doReturn(offsetDateTime).when(message).getCreationTime();
		doReturn(userName).when(user).getName();

		missLogs.put(user.getName(), misses);

		assertThat(missTracker.getMissLogs().get(userName).size(), is(1));

		missTracker.logMiss(message, FireTypes.GUNS);

		assertThat(missTracker.getMissLogs().get(userName).size(), is(2));
	}

	@Test
	public void testGetMissCountForUser_userNotInLog() {
		String userName = "user";
		doReturn(userName).when(user).getName();

		assertThat(missTracker.getMissCountForUser(user), is(0));
	}

	@Test
	public void testGetMissCountForUser_userInLog() {
		List<MissLog> misses = new ArrayList<>(2);
		misses.add(new MissLog());
		String userName = "user";
		doReturn(userName).when(user).getName();

		missLogs.put(user.getName(), misses);

		assertThat(missTracker.getMissCountForUser(user), is(1));
	}

	@Test
	public void testGetMostCurrentMissEvent_userNotInLog() {
		String userName = "user";
		doReturn(userName).when(user).getName();

		assertThat(missTracker.getMostCurrentMissEvent(user), is(nullValue()));
	}

	@Test
	public void testGetMostCurrentMissEvent_userInLog() {
		String userName = "user";
		doReturn(userName).when(user).getName();

		MissLog oldMiss = new MissLog(LocalDateTime.now(), FireTypes.GUNS);
		MissLog currentMiss = new MissLog(LocalDateTime.now(), FireTypes.FOX);

		missLogs.put(userName, Arrays.asList(oldMiss, currentMiss));

		assertThat(missTracker.getMostCurrentMissEvent(user), is(not(oldMiss)));
		assertThat(missTracker.getMostCurrentMissEvent(user), is(currentMiss));
	}

	@Test
	public void testIsValidShooter_userNotInLog() {
		String userName = "user";
		doReturn(user).when(message).getAuthor();
		doReturn(userName).when(user).getName();

		assertThat(missTracker.isValidShooter(message), is(true));
	}

	@Test
	public void testIsValidShooter_userInLog_and_coolDownHasPassed() {
		String userName = "user";
		doReturn(user).when(message).getAuthor();
		doReturn(userName).when(user).getName();

		FireTypes type = FireTypes.GUNS;
		LocalDateTime missTime = LocalDateTime.now();
		LocalDateTime messageTime = missTime.plusSeconds((long) (type.getCoolDown() + 1));

		doReturn(messageTime.atOffset(ZoneOffset.UTC)).when(message).getCreationTime();

		MissLog missLog = new MissLog(missTime, type);
		missLogs.put(userName, Arrays.asList(missLog));

		assertThat(missTracker.isValidShooter(message), is(true));
	}

	@Test
	public void testIsValidShooter_userInLog_and_coolDownHasNotPassed() {
		String userName = "user";
		doReturn(user).when(message).getAuthor();
		doReturn(userName).when(user).getName();

		FireTypes type = FireTypes.GUNS;
		LocalDateTime missTime = LocalDateTime.now();
		LocalDateTime messageTime = missTime.plusSeconds((long) (type.getCoolDown() - 1));

		doReturn(messageTime.atOffset(ZoneOffset.UTC)).when(message).getCreationTime();

		MissLog missLog = new MissLog(missTime, type);
		missLogs.put(userName, Arrays.asList(missLog));

		assertThat(missTracker.isValidShooter(message), is(false));
	}

	@Test
	public void testClearLogs() {
		missLogs.put("user", Arrays.asList());
		assertThat(missLogs.isEmpty(), is(false));
		missTracker.clearMisses();
		assertThat(missLogs.isEmpty(), is(true));
	}

	@Test
	public void testGetMissLogs() {
		assertThat(missTracker.getMissLogs(), sameInstance(missLogs));
	}

	@Test
	public void testSetMissLogs() {
		HashMap<String, List<MissLog>> newMissLogs = new HashMap<>();
		missTracker.setMissLogs(newMissLogs);
		assertThat(missTracker.getMissLogs(), sameInstance(newMissLogs));
	}
}
