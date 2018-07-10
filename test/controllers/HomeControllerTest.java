package controllers;

import akka.stream.IOResult;
import java.nio.file.Paths;
import java.io.File;
import akka.stream.Materializer;
import akka.stream.javadsl.FileIO;
import akka.stream.javadsl.Source;
import akka.util.ByteString;
import org.junit.Test;
import play.Application;
import play.libs.Files;
import play.mvc.Http;
import play.mvc.Result;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletionStage;

import static java.nio.file.Files.createTempFile;
import static java.nio.file.Files.write;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static play.test.Helpers.*;

public class HomeControllerTest {

    @Test
    public void testFileUpload() {
        Application app = fakeApplication();
        running(app, () -> {
                Files.TemporaryFileCreator temporaryFileCreator = app.injector().instanceOf(Files.TemporaryFileCreator.class);
                Materializer materializer = app.injector().instanceOf(Materializer.class);

                File file = new File("test\\resources\\ArtTutorGridPic.jpg");
                String absolutePath = file.getAbsolutePath();
                Path tempfilePath = Paths.get(absolutePath);

                Source<ByteString, CompletionStage<IOResult>> source = FileIO.fromPath(tempfilePath);
                Http.MultipartFormData.FilePart<Source<ByteString, ?>> part = new Http.MultipartFormData.FilePart<>("name", "filename", "image/jpeg", source);
                Http.RequestBuilder request = fakeRequest()
                        .method(POST)
                        .bodyMultipart(singletonList(part), temporaryFileCreator, materializer)
                        .uri("/upload");

                Result result = route(app, request);
                String actual = contentAsString(result);
                assertTrue(actual.toLowerCase().contains("Thanks for using our application to upload files to S3".toLowerCase()));
        });
    }
    
    @Test
    public void testIndex(){
    	 Application app = fakeApplication();
         running(app, () -> {
        	 Http.RequestBuilder request = fakeRequest()
                     .method(GET)
                     .uri("/index");
             Result result = route(app, request);
             String actual = contentAsString(result);
             assertNotNull(actual);
         });
    }

}
