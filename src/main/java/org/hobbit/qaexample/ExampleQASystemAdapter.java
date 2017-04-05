package org.hobbit.qaexample;

import java.io.IOException;

import org.aksw.qa.commons.load.json.ExtendedQALDJSONLoader;
import org.aksw.qa.commons.load.json.QaldJson;
import org.hobbit.core.components.AbstractSystemAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
public class ExampleQASystemAdapter extends AbstractSystemAdapter {
	
	
	private static Logger LOGGER = LoggerFactory.getLogger(ExampleQASystemAdapter.class);
	private ExampleQAAnnotator myAnnotator;
	
    public void init() throws Exception {
        super.init();
        
        myAnnotator= new ExampleQAAnnotator();
        
	    // Your initialization code comes here...
        // You can access the RDF model this.systemParamModel to retrieve meta data about this system adapter
    }

    /**
     * You MIGHT need this, depends on the benchmark.
     * @see <a href="https://project-hobbit.eu/challenges">Challenges and their Benchmarks</a>
     */
    public void receiveGeneratedData(byte[] data) {
        // handle the incoming data as described in the benchmark description
	}
    
    
    
    /**
     *  Create results for the incoming data.
     *  The structure of the incoming data should be defied by the <a href="https://project-hobbit.eu/challenges">Challenges and their Benchmarks</a>
     *
     *  e.g If you want to benchmark against the QALD-Challenge,
     *  you can expect the incoming data to be questions. Accordingly,
     *  your result[] output should be answers. 
     *  The data structure of the incoming and outgoing data should follow 
     *  the QALD-Json format. You can find <a href="https://github.com/AKSW/NLIWOD/tree/master/qa.commons/src/main/java/org/aksw/qa/commons/load">here</a>
     *   a loder and parser and a complete class structure for QALD-Json. 
     *   These are already included as dependency.
     *  
     *
     *  @see <a href="https://github.com/hobbit-project/platform/wiki/Develop-a-system-adapter#the-task-queue">The Task Queue and structure of data[]</a>
     *
     */
    public void receiveGeneratedTask(String taskId, byte[] data) {
    	
    	QaldJson inputJson=null;
		try {
			inputJson = (QaldJson)ExtendedQALDJSONLoader.readJson(data, QaldJson.class);
		} catch (IOException e1) {
			LOGGER.error("Couldn´t read input json",e1);
		}
    

    	
    	//Here is where your system has to do its job.
        QaldJson answerJson= (QaldJson) myAnnotator.annotate(inputJson);
        
        
        byte[] result=new byte[0];
		try {
			result = ExtendedQALDJSONLoader.writeJson(answerJson);
		} catch (JsonProcessingException e1) {
			// TODO handle malformed json
			e1.printStackTrace();
		}

		// Send the result to the evaluation storage
		try {
			sendResultToEvalStorage(taskId, result);
		} catch (IOException e) {
			//Log the error
		}
	}

    @Override
	public void close() throws IOException {
    	myAnnotator.close();
        super.close();
    }


}
