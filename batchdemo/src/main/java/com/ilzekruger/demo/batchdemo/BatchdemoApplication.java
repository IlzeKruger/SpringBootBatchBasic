package com.ilzekruger.demo.batchdemo;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class BatchdemoApplication {

	@Autowired
    JobLauncher jobLauncher;
      
    @Autowired
    Job job;
    
	@Value("${file.input}")
    private static String inputPath;

	public static void main(String[] args) {
		 SpringApplication.run(BatchdemoApplication.class, args);
	}
	
	//This is set to run every minute.
    @Scheduled(cron = "0 */1 * * * ?")   
    public void perform() throws Exception
    {
        JobParameters params = new JobParametersBuilder()
        		.addString("pathToFile", "/input/inputData.txt")
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(job, params);
    }

}

