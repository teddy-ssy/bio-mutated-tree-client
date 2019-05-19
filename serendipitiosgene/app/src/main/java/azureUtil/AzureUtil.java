package azureUtil;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by shengyuansun on 19/10/17.
 */

public class AzureUtil {

    String SAS = "?sv=2017-04-17&ss=b&srt=sco&sp=rwdlac&se=2017-10-20T10:24:03Z&st=2017-10-20T02:24:03Z&spr=https,http&sig=I2Exse3amWWp5wRorPqc5iD6Bb%2FU6c8Y7oTNT%2FWRtcs%3D";
    String Blob = "https://genesource.blob.core.windows.net/?sv=2017-04-17&ss=b&srt=sco&sp=rwdlac&se=2017-10-20T10:24:03Z&st=2017-10-20T02:24:03Z&spr=https,http&sig=I2Exse3amWWp5wRorPqc5iD6Bb%2FU6c8Y7oTNT%2FWRtcs%3D";
    String Key1 = "aHnqrVL71EJw72FPkq+G9wHXS22FXkivnb6UEOwzmboY6VGkgBfue/b/cGSRZXc6T1YV4iblnTY+AJPePDwEOw==";
    String Key2 = "Ii2T2zYzQ/Xr9SAzG7xcIoG0pYljZYv+cV33s+nSqiLRSgWt3/iSkrzxmDiQsrtqdifWBjN+0WWENzLfVVAcPw==";
    String name= "genesource";
    public static final String storageConnectionString = "DefaultEndpointsProtocol=https;"
            + "AccountName=genesource;"
            + "AccountKey=aHnqrVL71EJw72FPkq+G9wHXS22FXkivnb6UEOwzmboY6VGkgBfue/b/cGSRZXc6T1YV4iblnTY+AJPePDwEOw==";

    private static CloudBlobContainer getContainer() throws Exception {
        // Retrieve storage account from connection-string.

        CloudStorageAccount storageAccount = CloudStorageAccount
                .parse(storageConnectionString);

        // Create the blob client.
        CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

        // Get a reference to a container.
        // The container name must be lower case
        CloudBlobContainer container = blobClient.getContainerReference("genesource");
        //container.createIfNotExists();

        return container;
    }

    private static CloudBlobContainer getContainer(String contrainer) throws Exception {
        // Retrieve storage account from connection-string.

        CloudStorageAccount storageAccount = CloudStorageAccount
                .parse(storageConnectionString);

        // Create the blob client.
        CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

        // Get a reference to a container.
        // The container name must be lower case
        CloudBlobContainer container = blobClient.getContainerReference(contrainer);
        //container.createIfNotExists();

        return container;
    }

    public static String Uploadfasta(InputStream image, int imageLength,String name) throws Exception {
        CloudBlobContainer container = getContainer();

        container.createIfNotExists();

        CloudBlockBlob imageBlob = container.getBlockBlobReference(name);
        imageBlob.upload(image, imageLength);

        return name;

    }
    public static String Uploadfasta(File file, String name) throws Exception {
        CloudBlobContainer container = getContainer();

        container.createIfNotExists();

        CloudBlockBlob imageBlob = container.getBlockBlobReference(name);
        imageBlob.uploadFromFile(file.getPath());

        return name;

    }

    public static String[] Listallfile() throws Exception{
        CloudBlobContainer container = getContainer();

        Iterable<ListBlobItem> blobs = container.listBlobs();

        LinkedList<String> blobNames = new LinkedList<>();
        for(ListBlobItem blob: blobs) {
            blobNames.add(((CloudBlockBlob) blob).getName());
        }

        return blobNames.toArray(new String[blobNames.size()]);
    }

    public static String[] ListallfilePath() throws Exception{
        CloudBlobContainer container = getContainer();

        Iterable<ListBlobItem> blobs = container.listBlobs();

        LinkedList<String> blobPaths = new LinkedList<>();
        for(ListBlobItem blob: blobs) {
            blobPaths.add(((CloudBlockBlob) blob).getUri().toString());
        }

        return blobPaths.toArray(new String[blobPaths.size()]);
    }

    public static void GetFile(String name, OutputStream imageStream, long imageLength) throws Exception {
        CloudBlobContainer container = getContainer();

        CloudBlockBlob blob = container.getBlockBlobReference(name);

        if(blob.exists()){
            blob.downloadAttributes();

            imageLength = blob.getProperties().getLength();

            blob.download(imageStream);
        }
    }

    static final String validChars = "abcdefghijklmnopqrstuvwxyz";
    //static SecureRandom rnd = new SecureRandom();
}
