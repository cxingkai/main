package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.ReadOnlyIMDB;

/**
 * A class to access IMDB data stored as an xml file on the hard disk.
 */
public class XmlIMDBStorage implements IMDBStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlIMDBStorage.class);

    private String filePath;

    public XmlIMDBStorage(String filePath) {
        this.filePath = filePath;
    }

    public String getAddressBookFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyIMDB> readAddressBook() throws DataConversionException, IOException {
        return readAddressBook(filePath);
    }

    /**
     * Similar to {@link #readAddressBook()}
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyIMDB> readAddressBook(String filePath) throws DataConversionException,
                                                                                 FileNotFoundException {
        requireNonNull(filePath);

        File addressBookFile = new File(filePath);

        if (!addressBookFile.exists()) {
            logger.info("IMDB file "  + addressBookFile + " not found");
            return Optional.empty();
        }

        XmlSerializableIMDB xmlAddressBook = XmlFileStorage.loadDataFromSaveFile(new File(filePath));
        try {
            return Optional.of(xmlAddressBook.toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + addressBookFile + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void backupAddressBook(ReadOnlyIMDB addressBook) throws IOException {
        saveAddressBook(addressBook, filePath + ".backup");
    }

    @Override
    public void saveAddressBook(ReadOnlyIMDB addressBook) throws IOException {
        saveAddressBook(addressBook, filePath);
    }

    /**
     * Similar to {@link #saveAddressBook(ReadOnlyIMDB)}
     * @param filePath location of the data. Cannot be null
     */
    public void saveAddressBook(ReadOnlyIMDB addressBook, String filePath) throws IOException {
        requireNonNull(addressBook);
        requireNonNull(filePath);

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        XmlFileStorage.saveDataToFile(file, new XmlSerializableIMDB(addressBook));
    }

}
