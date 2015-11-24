package libsvm;

import libsvm.*;
import java.io.*;
import java.util.*;
import sanchay.GlobalProperties;

class SVMPredictModified{

    
	protected DataOutputStream outputOb;
        protected SVMModel modelOb;
        protected int predictProbability;
        protected String inputLine = new String();
        
    private String getInputLine() {
       return inputLine;
    }
    private void setInputLine(String line) {
        inputLine = line;
    }
    
    
        private static double atof(String s)
	{
		return Double.valueOf(s).doubleValue();
	}

	private static int atoi(String s)
	{
		return Integer.parseInt(s);
	}

	private static void predict(String input, DataOutputStream output, SVMModel model, int predict_probability) throws IOException
	{
		int correct = 0;
		int total = 0;
		double error = 0;
		double sumv = 0, sumy = 0, sumvv = 0, sumyy = 0, sumvy = 0;

		int svm_type=SVM.svm_get_svm_type(model);
		int nr_class=SVM.svm_get_nr_class(model);
		double[] prob_estimates=null;

		if(predict_probability == 1)
		{
			if(svm_type == SVMParameter.EPSILON_SVR ||
			   svm_type == SVMParameter.NU_SVR)
			{
				System.out.print(GlobalProperties.getIntlString("Prob._model_for_test_data:_target_value_=_predicted_value_+_z,\nz:_Laplace_distribution_e^(-|z|/sigma)/(2sigma),sigma=")+SVM.svm_get_svr_probability(model)+GlobalProperties.getIntlString("\n"));
			}
			else
			{
				int[] labels=new int[nr_class];
				SVM.svm_get_labels(model,labels);
				prob_estimates = new double[nr_class];
				output.writeBytes(GlobalProperties.getIntlString("labels"));
				for(int j=0;j<nr_class;j++)
					output.writeBytes(GlobalProperties.getIntlString("_")+labels[j]);
				output.writeBytes(GlobalProperties.getIntlString("\n"));
			}
		}
		//while(true)
		{
			String line = input;
			//if(line == null) break;

			StringTokenizer st = new StringTokenizer(line,GlobalProperties.getIntlString("_\t\n\r\f:"));

			double target = atof(st.nextToken());
			int m = st.countTokens()/2;
			SVMNode[] x = new SVMNode[m];
			for(int j=0;j<m;j++)
			{
				x[j] = new SVMNode();
				x[j].index = atoi(st.nextToken());
				x[j].value = atof(st.nextToken());
			}

			double v;
			if (predict_probability==1 && (svm_type==SVMParameter.C_SVC || svm_type==SVMParameter.NU_SVC))
			{
				v = SVM.svm_predict_probability(model,x,prob_estimates);
				output.writeBytes(v+GlobalProperties.getIntlString("_"));
				for(int j=0;j<nr_class;j++)
					output.writeBytes(prob_estimates[j]+GlobalProperties.getIntlString("_"));
				output.writeBytes(GlobalProperties.getIntlString("\n"));
			}
			else
			{
				v = SVM.svm_predict(model,x);
				output.writeBytes(v+GlobalProperties.getIntlString("\n"));
                                System.out.println(GlobalProperties.getIntlString("___classification_output-")+v);
			}

			if(v == target)
				++correct;
			error += (v-target)*(v-target);
			sumv += v;
			sumy += target;
			sumvv += v*v;
			sumyy += target*target;
			sumvy += v*target;
			++total;
		}
		if(svm_type == SVMParameter.EPSILON_SVR ||
		   svm_type == SVMParameter.NU_SVR)
		{
			System.out.print(GlobalProperties.getIntlString("Mean_squared_error_=_")+error/total+" (regression)\n");
			System.out.print(GlobalProperties.getIntlString("Squared_correlation_coefficient_=_")+
				 ((total*sumvy-sumv*sumy)*(total*sumvy-sumv*sumy))/
				 ((total*sumvv-sumv*sumv)*(total*sumyy-sumy*sumy))+
				 GlobalProperties.getIntlString("_(regression)\n"));
		}
		else
			System.out.print(GlobalProperties.getIntlString("Accuracy_=_")+(double)correct/total*100+
				 "% ("+correct+"/"+total+") (classification)\n");
	}

	private static void exit_with_help()
	{
		System.err.print("usage: svm_predict [options] test_file model_file output_file\n"
		+"options:\n"
		+"-b probability_estimates: whether to predict probability estimates, 0 or 1 (default 0); one-class SVM not supported yet\n");
		System.exit(1);
	}

	public void main(String argv[]) throws IOException
	{
		int i, predict_probability=0;

		// parse options
		for(i=0;i<argv.length;i++)
		{
			if(argv[i].charAt(0) != '-') break;
			++i;
			switch(argv[i-1].charAt(1))
			{
				case 'b':
					predict_probability = atoi(argv[i]);
					break;
				default:
					System.err.print("Unknown option: " + argv[i-1] + "\n");
					exit_with_help();
			}
		}
		if(i>=argv.length-2)
			exit_with_help();
		try 
		{
			BufferedReader input = new BufferedReader(new FileReader(argv[i]));
			DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(argv[i+2])));
			SVMModel model = SVM.svm_load_model(argv[i+1]);
			if(predict_probability == 1)
			{
				if(SVM.svm_check_probability_model(model)==0)
				{
					System.err.print("Model does not support probabiliy estimates\n");
					System.exit(1);
				}
			}
			else
			{
				if(SVM.svm_check_probability_model(model)!=0)
				{
					System.out.print("Model supports probability estimates, but disabled in prediction.\n");
				}
			} 
                        getInputLine();
			predict(getInputLine(),output,model,predict_probability);
			input.close();
			output.close();
		} 
		catch(FileNotFoundException e) 
		{
			exit_with_help();
		}
		catch(ArrayIndexOutOfBoundsException e) 
		{
			exit_with_help();
		}
	}
}
