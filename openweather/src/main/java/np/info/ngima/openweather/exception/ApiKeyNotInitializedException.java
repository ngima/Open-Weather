package np.info.ngima.openweather.exception;

import androidx.annotation.Nullable;

public class ApiKeyNotInitializedException extends Exception{

    @Nullable
    @Override
    public String getMessage() {
        return "ApiKeyNotInitializedException seems like you didn't set API Key";
    }
}
