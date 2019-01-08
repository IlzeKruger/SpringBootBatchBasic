package com.ilzekruger.demo.batchdemo;


import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Configuration
@EnableBatchProcessing
public class BatchConfigurationProcessing extends DefaultBatchConfigurer{

    private static int maxThreads = 5; 
    private static final String OVERRIDDEN_BY_EXPRESSION = null;
    
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    private Resource outputResource = new FileSystemResource("output/encodedOutputData.txt");
     
    // tag::readerwriterprocessor[]    
	@SuppressWarnings("null")
	@Bean
	@StepScope
	public FlatFileItemReader<String> reader(
			@Value("#{jobParameters[pathToFile]}") String pathToFile){
		FlatFileItemReader<String> itemReader = new FlatFileItemReader<String>();
		itemReader.setLineMapper(new PassThroughLineMapper());
		System.out.println("Found file as : " + pathToFile);
		itemReader.setResource(new ClassPathResource(pathToFile));
		return itemReader;
	}
	
	@Bean
    public LineItemProcessor processor() {
        return new LineItemProcessor();
    }
	   
	@Bean
    public FlatFileItemWriter<String> writer()
    {
		  return new FlatFileItemWriterBuilder<String>()
			        .name("greetingItemWriter")
			        .resource(outputResource)
			        .lineAggregator(new PassThroughLineAggregator<>()).build();
    }
    // end::readerwriterprocessor[]
	
	// tag::jobstep[]
    @Bean
    public Job covertFlatFileToCipherEncodedFlatFile(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("covertFlatFileToCipherEncodedFlatFile")
            .incrementer(new RunIdIncrementer())
            .listener(listener)
            .flow(step1)
            .end()
            .build();
    }
	
    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
            .<String, String>chunk(2)
            .reader(reader(OVERRIDDEN_BY_EXPRESSION))
            .processor(processor())
            .writer(writer())
            .build();
    }
    // end::jobstep[]
    
}
