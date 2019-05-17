package codemetropolis.toolchain.rendering;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;

import org.junit.Test;

import codemetropolis.toolchain.rendering.events.ProgressEvent;
import codemetropolis.toolchain.rendering.events.ProgressEventListener;

public class RenderingExecutorTest {

	@Test
	public void overridePermissionTestWithAccept() {

		RenderingExecutor renderingExecutor = new RenderingExecutor();

		ByteArrayInputStream in = new ByteArrayInputStream("y".getBytes());
		System.setIn(in);
		boolean overridePermission = renderingExecutor.overridePermission();
		assertTrue(overridePermission);
	}

	@Test
	public void overridePermissionTestWithDenied() {

		RenderingExecutor renderingExecutor = new RenderingExecutor();

		ByteArrayInputStream in = new ByteArrayInputStream("n".getBytes());
		System.setIn(in);
		boolean overridePermission = renderingExecutor.overridePermission();
		assertTrue(!overridePermission);
	}

	@Test
	public void addEventListenerTest() {

		RenderingExecutor renderingExecutor = new RenderingExecutor();
		int listenerCount = renderingExecutor.getListenerCount();
		ProgressEventListener listener = new ProgressEventListener() {

			@Override
			public void onNextState(ProgressEvent event) {
			}
		};

		renderingExecutor.addEventListener(listener);
		assertTrue(listenerCount + 1 == (renderingExecutor.getListenerCount()));

	}

	@Test
	public void removeEventListenerTest() {

		RenderingExecutor renderingExecutor = new RenderingExecutor();
		int listenerCount = renderingExecutor.getListenerCount();
		ProgressEventListener listener = new ProgressEventListener() {

			@Override
			public void onNextState(ProgressEvent event) {
			}
		};

		renderingExecutor.addEventListener(listener);
		assertTrue(listenerCount + 1 == (renderingExecutor.getListenerCount()));
		
		
		renderingExecutor.removeEventListener(listener);
		assertTrue(listenerCount == (renderingExecutor.getListenerCount()));

	}

}
