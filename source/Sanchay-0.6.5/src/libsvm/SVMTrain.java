package libsvm;

import libsvm.*;
import java.io.*;
import java.util.*;
import sanchay.GlobalProperties;

public class SVMTrain {
	private SVMParameter param;		// set by parse_command_line
	private SVMProblem prob;		// set by read_problem
	private SVMModel model;
	private String input_file_name;		// set by parse_command_line
	private String model_file_name;		// set by parse_command_line
	private String error_msg;
	private int cross_validation;
	private int nr_fold;

	private static void exit_with_help()
	{
		System.out.print(
		 "Usage:_SVMTrain_[options]_training_set_file_[model_file]\n"
		+"options:\n"
		+"-s_svm_type_:_set_type_of_SVM_(default_0)\n"
		+"	0_--_C-SVC\n"
		+"	1_--_nu-SVC\n"
		+"	2_--_one-class_SVM\n"
		+"	3_--_epsilon-SVR\n"
		+"	4_--_nu-SVR\n"
		+"-t_kernel_type_:_set_type_of_kernel_function_(default_2)\n"
		+"	0_--_linear:_u'*v\n"
		+"	1_--_polynomial:_(gamma*u'*v_+_coef0)^degree\n"
		+"	2_--_radial_basis_function:_exp(-gamma*|u-v|^2)\n"
		+"	3_--_sigmoid:_tanh(gamma*u'*v_+_coef0)\n"
		+"	4_--_precomputed_kernel_(kernel_values_in_training_set_file)\n"
		+"-d_degree_:_set_degree_in_kernel_function_(default_3)\n"
		+"-g_gamma_:_set_gamma_in_kernel_function_(default_1/k)\n"
		+"-r_coef0_:_set_coef0_in_kernel_function_(default_0)\n"
		+"-c_cost_:_set_the_parameter_C_of_C-SVC,_epsilon-SVR,_and_nu-SVR_(default_1)\n"
		+"-n_nu_:_set_the_parameter_nu_of_nu-SVC,_one-class_SVM,_and_nu-SVR_(default_0.5)\n"
		+"-p_epsilon_:_set_the_epsilon_in_loss_function_of_epsilon-SVR_(default_0.1)\n"
		+"-m_cachesize_:_set_cache_memory_size_in_MB_(default_100)\n"
		+"-e_epsilon_:_set_tolerance_of_termination_criterion_(default_0.001)\n"
		+"-h_shrinking_:_whether_to_use_the_shrinking_heuristics,_0_or_1_(default_1)\n"
		+"-b_probability_estimates_:_whether_to_train_a_SVC_or_SVR_model_for_probability_estimates,_0_or_1_(default_0)\n"
		+"-wi_weight_:_set_the_parameter_C_of_class_i_to_weight*C,_for_C-SVC_(default_1)\n"
		+"-v_n_:_n-fold_cross_validation_mode\n"
		+"-q_:_quiet_mode_(no_outputs)\n"
		);
		System.exit(1);
	}

	private void do_cross_validation()
	{
		int i;
		int total_correct = 0;
		double total_error = 0;
		double sumv = 0, sumy = 0, sumvv = 0, sumyy = 0, sumvy = 0;
		double[] target = new double[prob.l];

		SVM.svm_cross_validation(prob,param,nr_fold,target);
		if(param.svm_type == SVMParameter.EPSILON_SVR ||
		   param.svm_type == SVMParameter.NU_SVR)
		{
			for(i=0;i<prob.l;i++)
			{
				double y = prob.y[i];
				double v = target[i];
				total_error += (v-y)*(v-y);
				sumv += v;
				sumy += y;
				sumvv += v*v;
				sumyy += y*y;
				sumvy += v*y;
			}
			System.out.print(GlobalProperties.getIntlString("Cross_Validation_Mean_squared_error_=_")+total_error/prob.l+"\n");
			System.out.print(GlobalProperties.getIntlString("Cross_Validation_Squared_correlation_coefficient_=_")+
				((prob.l*sumvy-sumv*sumy)*(prob.l*sumvy-sumv*sumy))/
				((prob.l*sumvv-sumv*sumv)*(prob.l*sumyy-sumy*sumy))+ "\n"
				);
		}
		else
		{
			for(i=0;i<prob.l;i++)
				if(target[i] == prob.y[i])
					++total_correct;
			System.out.print(GlobalProperties.getIntlString("Cross_Validation_Accuracy_=_")+100.0*total_correct/prob.l+"%\n");
		}
	}
	
	private void run(String argv[]) throws IOException
	{
		parse_command_line(argv);
		read_problem();
		error_msg = SVM.svm_check_parameter(prob,param);

		if(error_msg != null)
		{
			System.err.print(GlobalProperties.getIntlString("Error:_")+error_msg+"\n");
			System.exit(1);
		}

		if(cross_validation != 0)
		{
			do_cross_validation();
		}
		else
		{
			model = SVM.svm_train(prob,param);
			SVM.svm_save_model(model_file_name,model);
		}
	}

	public static void main(String argv[]) throws IOException
	{
		SVMTrain t = new SVMTrain();
		t.run(argv);
	}

	private static double atof(String s)
	{
		double d = Double.valueOf(s).doubleValue();
		if (Double.isNaN(d) || Double.isInfinite(d))
		{
			System.err.print("NaN or Infinity in input\n");
			System.exit(1);
		}
		return(d);
	}

	private static int atoi(String s)
	{
		return Integer.parseInt(s);
	}

	private void parse_command_line(String argv[])
	{
		int i;

		param = new SVMParameter();
		// default values
		param.svm_type = SVMParameter.C_SVC;
		param.kernel_type = SVMParameter.RBF;
		param.degree = 3;
		param.gamma = 0;	// 1/k
		param.coef0 = 0;
		param.nu = 0.5;
		param.cache_size = 100;
		param.C = 1;
		param.eps = 1e-3;
		param.p = 0.1;
		param.shrinking = 1;
		param.probability = 0;
		param.nr_weight = 0;
		param.weight_label = new int[0];
		param.weight = new double[0];
		cross_validation = 0;

		// parse options
		for(i=0;i<argv.length;i++)
		{
			if(argv[i].charAt(0) != '-') break;
			if(++i>=argv.length)
				exit_with_help();
			switch(argv[i-1].charAt(1))
			{
				case 's':
					param.svm_type = atoi(argv[i]);
					break;
				case 't':
					param.kernel_type = atoi(argv[i]);
					break;
				case 'd':
					param.degree = atoi(argv[i]);
					break;
				case 'g':
					param.gamma = atof(argv[i]);
					break;
				case 'r':
					param.coef0 = atof(argv[i]);
					break;
				case 'n':
					param.nu = atof(argv[i]);
					break;
				case 'm':
					param.cache_size = atof(argv[i]);
					break;
				case 'c':
					param.C = atof(argv[i]);
					break;
				case 'e':
					param.eps = atof(argv[i]);
					break;
				case 'p':
					param.p = atof(argv[i]);
					break;
				case 'h':
					param.shrinking = atoi(argv[i]);
					break;
				case 'b':
					param.probability = atoi(argv[i]);
					break;
				case 'q':
//					SVM.svm_print_string = new svm_print_interface()
//					{
//						public void print(String s){}
//					};
//					i--;
					break;
				case 'v':
					cross_validation = 1;
					nr_fold = atoi(argv[i]);
					if(nr_fold < 2)
					{
						System.err.print("n-fold cross validation: n must >= 2\n");
						exit_with_help();
					}
					break;
				case 'w':
					++param.nr_weight;
					{
						int[] old = param.weight_label;
						param.weight_label = new int[param.nr_weight];
						System.arraycopy(old,0,param.weight_label,0,param.nr_weight-1);
					}

					{
						double[] old = param.weight;
						param.weight = new double[param.nr_weight];
						System.arraycopy(old,0,param.weight,0,param.nr_weight-1);
					}

					param.weight_label[param.nr_weight-1] = atoi(argv[i-1].substring(2));
					param.weight[param.nr_weight-1] = atof(argv[i]);
					break;
				default:
					System.err.print(GlobalProperties.getIntlString("Unknown_option:_") + argv[i-1] + "\n");
					exit_with_help();
			}
		}

		// determine filenames

		if(i>=argv.length)
			exit_with_help();

		input_file_name = argv[i];

		if(i<argv.length-1)
			model_file_name = argv[i+1];
		else
		{
			int p = argv[i].lastIndexOf('/');
			++p;	// whew...
			model_file_name = argv[i].substring(p)+GlobalProperties.getIntlString(".model");
		}
	}

	// read in a problem (in svmlight format)

	private void read_problem() throws IOException
	{
		BufferedReader fp = new BufferedReader(new FileReader(input_file_name));
		Vector<Double> vy = new Vector<Double>();
		Vector<SVMNode[]> vx = new Vector<SVMNode[]>();
		int max_index = 0;

		while(true)
		{
			String line = fp.readLine();
			if(line == null) break;

			StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");

			vy.addElement(atof(st.nextToken()));
			int m = st.countTokens()/2;
			SVMNode[] x = new SVMNode[m];
			for(int j=0;j<m;j++)
			{
				x[j] = new SVMNode();
				x[j].index = atoi(st.nextToken());
				x[j].value = atof(st.nextToken());
			}
			if(m>0) max_index = Math.max(max_index, x[m-1].index);
			vx.addElement(x);
		}

		prob = new SVMProblem();
		prob.l = vy.size();
		prob.x = new SVMNode[prob.l][];
		for(int i=0;i<prob.l;i++)
			prob.x[i] = vx.elementAt(i);
		prob.y = new double[prob.l];
		for(int i=0;i<prob.l;i++)
			prob.y[i] = vy.elementAt(i);

		if(param.gamma == 0 && max_index > 0)
			param.gamma = 1.0/max_index;

		if(param.kernel_type == SVMParameter.PRECOMPUTED)
			for(int i=0;i<prob.l;i++)
			{
				if (prob.x[i][0].index != 0)
				{
					System.err.print("Wrong kernel matrix: first column must be 0:sample serial number\n");
					System.exit(1);
				}
				if ((int)prob.x[i][0].value <= 0 || (int)prob.x[i][0].value > max_index)
				{
					System.err.print("Wrong input format: sample serial number out of range\n");
					System.exit(1);
				}
			}

		fp.close();
	}
}
