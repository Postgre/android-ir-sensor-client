package es.alvaroweb.ircamerareader;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class WebsocketIntentService extends IntentService {
    private static final String ACTION_CONNECT_TO_WEBSOCKET = "es.alvaroweb.ircamerareader.action.WEBSOCKET";

    private static final String URI_PARAM = "es.alvaroweb.ircamerareader.extra.PARAM1";

    public WebsocketIntentService() {
        super("WebsocketIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionWebsocket(Context context, String uri, String param2) {
        Intent intent = new Intent(context, WebsocketIntentService.class);
        intent.setAction(ACTION_CONNECT_TO_WEBSOCKET);
        intent.putExtra(URI_PARAM, uri);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CONNECT_TO_WEBSOCKET.equals(action)) {
                final String uri = intent.getStringExtra(URI_PARAM);
                handleActionFoo(uri);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String uri) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
