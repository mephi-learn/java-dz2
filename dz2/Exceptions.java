package dz2;

class InvalidArgumentException extends Exception {
    public InvalidArgumentException(String message) {
        super("Invalid argument: " + message);
    }
}

class InvalidLatitudeException extends Exception {
    public InvalidLatitudeException(String message) {
        super("Invalid latitude: " + message);
    }
}

class InvalidLongitudeException extends Exception {
    public InvalidLongitudeException(String message) {
        super("Invalid longitude: " + message);
    }
}

class InvalidLimitException extends Exception {
    public InvalidLimitException(String message) {
        super("Invalid limit: " + message);
    }
}

class InvalidKeyException extends Exception {
    public InvalidKeyException(String message) {
        super("Invalid key: " + message);
    }
}

class InvalidQueryException extends Exception {
    public InvalidQueryException(String message) {
        super("Invalid query: " + message);
    }
}

