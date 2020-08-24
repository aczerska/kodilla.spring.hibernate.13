package com.kodilla.homework13;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.support.FileExistsMode;

import java.io.File;

@Configuration
public class IntegrationFlowConfiguration {
    @Bean
    IntegrationFlow fileIntegrationFlow(FileReadingMessageSource fileAdapter,
                                        FileTransformer transformer,
                                        FileWritingMessageHandler handler) {
        return IntegrationFlows.from(fileAdapter, config -> config.poller(Pollers.fixedDelay(1000)))
                .transform(transformer, "transformFile")
                .handle(handler)
                .get();
    }

    @Bean
    FileReadingMessageSource fileAdapter(){
        FileReadingMessageSource fileSource = new FileReadingMessageSource();
        fileSource.setDirectory(new File("data/input"));
        return fileSource;
    }

    @Bean
    FileTransformer transformer() {
        return new FileTransformer();
    }

    @Bean
    FileWritingMessageHandler outputFileAdapter() {
        final String anyFilename = "fooTestFileName";
        File directory = new File("data/output");
        FileWritingMessageHandler handler = new FileWritingMessageHandler(directory);
        handler.setExpectReply(false);
        handler.setFileExistsMode(FileExistsMode.APPEND);
        handler.setAppendNewLine(true);
        handler.setFileNameGenerator(message -> anyFilename);

        return handler;
    }

}
