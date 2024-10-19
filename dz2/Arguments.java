package dz2;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;

class Arguments {
    String[] args;

    public Arguments(String @NotNull [] args) {
        this.args = args;
    }

    public String GetKey() throws InvalidArgumentException, InvalidKeyException {
        if (args == null) {
            throw new InvalidArgumentException("no data");
        }
        return GetKey(args);
    }

    public static String GetKey(String @NotNull [] args) throws InvalidKeyException {
        if (args.length == 0) {
            throw new InvalidKeyException("no data");
        }
        String value;
        if (isValidUUID((value = args[0].trim()))) {
            return value;
        }
        File file = new File(value);
        if (!file.exists()) {
            throw new InvalidKeyException("file not found");
        }
        try (Scanner sc = new Scanner(file)) {
            if (isValidUUID((value = sc.nextLine().trim()))) {
                return value;
            }
        } catch (IOException e) {
            throw new InvalidKeyException("invalid file");
        }
        throw new InvalidKeyException("file has invalid key");
    }

    public double GetLatitude() throws InvalidArgumentException, InvalidLatitudeException {
        if (args == null) {
            throw new InvalidArgumentException("no data");
        }
        return GetLatitude(args);
    }

    public static double GetLatitude(String @NotNull [] args) throws InvalidLatitudeException {
        if (args.length < 3) {
            return -1;
        }
        try {
            return Double.parseDouble(args[1].trim());

        } catch (NumberFormatException e) {
            throw new InvalidLatitudeException("latitude not number");
        }
    }

    public double GetLongitude() throws InvalidArgumentException, InvalidLongitudeException {
        if (args == null) {
            throw new InvalidArgumentException("no data");
        }
        return GetLongitude(args);
    }

    public static double GetLongitude(String @NotNull [] args) throws InvalidLongitudeException {
        if (args.length < 3) {
            return -1;
        }
        try {
            return Double.parseDouble(args[2].trim());

        } catch (NumberFormatException e) {
            throw new InvalidLongitudeException("longitude not number");
        }
    }

    public int GetLimit() throws InvalidArgumentException, InvalidLimitException {
        if (args == null) {
            throw new InvalidArgumentException("no data");
        }
        return GetLimit(args);
    }

    public static int GetLimit(String @NotNull [] args) throws InvalidLimitException {
        // Лимит может быть или вторым параметром (при отсутствии широты/долготы), так и четвёртым (при их наличии)
        if (args.length != 2 && args.length != 4) {
            return 0;
        }
        try {
            int limit = Integer.parseInt(args[1].trim());
            if (args.length == 4) {
                limit = Integer.parseInt(args[3].trim());
            }

            // Лимит не может быть более 7, ибо это максимальное количество дней в выборке
            if (limit < 0 || limit > 7) {
                throw new InvalidLimitException("limit must be between 0 (all values) and 7");
            }
            return limit;

        } catch (NumberFormatException e) {
            throw new InvalidLimitException("limit not number");
        }
    }

    private static boolean isValidUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
