import java.io.*;
import java.util.*;
import sanchay.GlobalProperties;

class SVMScale
{
	private String line = null;
	private double lower = -1.0;
	private double upper = 1.0;
	private double y_lower;
	private double y_upper;
	private boolean y_scaling = false;
	private double[] feature_max;
	private double[] feature_min;
	private double y_max = -Double.MAX_VALUE;
	private double y_min = Double.MAX_VALUE;
	private int max_index;
	private long num_nonzeros = 0;
	private long new_num_nonzeros = 0;

	private static void exit_with_help()
	{
		System.out.print(
		 GlobalProperties.getIntlString("Usage:_svm-scale_[options]_data_filename\n")
		+GlobalProperties.getIntlString("options:\n")
		+GlobalProperties.getIntlString("-l_lower_:_x_scaling_lower_limit_(default_-1)\n")
		+GlobalProperties.getIntlString("-u_upper_:_x_scaling_upper_limit_(default_+1)\n")
		+GlobalProperties.getIntlString("-y_y_lower_y_upper_:_y_scaling_limits_(default:_no_y_scaling)\n")
		+GlobalProperties.getIntlString("-s_save_filename_:_save_scaling_parameters_to_save_filename\n")
		+GlobalProperties.getIntlString("-r_restore_filename_:_restore_scaling_parameters_from_restore_filename\n")
		);
		System.exit(1);
	}

	private BufferedReader rewind(BufferedReader fp, String filename) throws IOException
	{
		fp.close();
		return new BufferedReader(new FileReader(filename));
	}

	private void output_target(double value)
	{
		if(y_scaling)
		{
			if(value == y_min)
				value = y_lower;
			else if(value == y_max)
				value = y_upper;
			else
				value = y_lower + (y_upper-y_lower) *
				(value-y_min) / (y_max-y_min);
		}

		System.out.print(value + GlobalProperties.getIntlString("_"));
	}

	private void output(int index, double value)
	{
		/* skip single-valued attribute */
		if(feature_max[index] == feature_min[index])
			return;

		if(value == feature_min[index])
			value = lower;
		else if(value == feature_max[index])
			value = upper;
		else
			value = lower + (upper-lower) * 
				(value-feature_min[index])/
				(feature_max[index]-feature_min[index]);

		if(value != 0)
		{
			System.out.print(index + GlobalProperties.getIntlString(":") + value + GlobalProperties.getIntlString("_"));
			new_num_nonzeros++;
		}
	}

	private String readline(BufferedReader fp) throws IOException
	{
		line = fp.readLine();
		return line;
	}

	private void run(String []argv) throws IOException
	{
		int i,index;
		BufferedReader fp = null, fp_restore = null;
		String save_filename = null;
		String restore_filename = null;
		String data_filename = null;


		for(i=0;i<argv.length;i++)
		{
			if (argv[i].charAt(0) != '-')	break;
			++i;
			switch(argv[i-1].charAt(1))
			{
				case 'l': lower = Double.parseDouble(argv[i]);	break;
				case 'u': upper = Double.parseDouble(argv[i]);	break;
				case 'y':
					  y_lower = Double.parseDouble(argv[i]);
					  ++i;
					  y_upper = Double.parseDouble(argv[i]);
					  y_scaling = true;
					  break;
				case 's': save_filename = argv[i];	break;
				case 'r': restore_filename = argv[i];	break;
				default:
					  System.err.println(GlobalProperties.getIntlString("unknown_option"));
					  exit_with_help();
			}
		}

		if(!(upper > lower) || (y_scaling && !(y_upper > y_lower)))
		{
			System.err.println(GlobalProperties.getIntlString("inconsistent_lower/upper_specification"));
			System.exit(1);
		}
		if(restore_filename != null && save_filename != null)
		{
			System.err.println(GlobalProperties.getIntlString("cannot_use_-r_and_-s_simultaneously"));
			System.exit(1);
		}

		if(argv.length != i+1)
			exit_with_help();

		data_filename = argv[i];
		try {
			fp = new BufferedReader(new FileReader(data_filename));
		} catch (Exception e) {
			System.err.println(GlobalProperties.getIntlString("can't_open_file_") + data_filename);
			System.exit(1);
		}

		/* assumption: min index of attributes is 1 */
		/* pass 1: find out max index of attributes */
		max_index = 0;

		if(restore_filename != null)
		{
			int idx, c;

			try {
				fp_restore = new BufferedReader(new FileReader(restore_filename));
			}
			catch (Exception e) {
				System.err.println(GlobalProperties.getIntlString("can't_open_file_") + restore_filename);
				System.exit(1);
			}
			if((c = fp_restore.read()) == 'y')
			{
				fp_restore.readLine();
				fp_restore.readLine();		
				fp_restore.readLine();		
			}
			fp_restore.readLine();
			fp_restore.readLine();

			String restore_line = null;
			while((restore_line = fp_restore.readLine())!=null)
			{
				StringTokenizer st2 = new StringTokenizer(restore_line);
				idx = Integer.parseInt(st2.nextToken());
				max_index = Math.max(max_index, idx);
			}
			fp_restore = rewind(fp_restore, restore_filename);
		}

		while (readline(fp) != null)
		{
			StringTokenizer st = new StringTokenizer(line,GlobalProperties.getIntlString("_\t\n\r\f:"));
			st.nextToken();
			while(st.hasMoreTokens())
			{
				index = Integer.parseInt(st.nextToken());
				max_index = Math.max(max_index, index);
				st.nextToken();
				num_nonzeros++;
			}
		}

		try {
			feature_max = new double[(max_index+1)];
			feature_min = new double[(max_index+1)];
		} catch(OutOfMemoryError e) {
			System.err.println(GlobalProperties.getIntlString("can't_allocate_enough_memory"));
			System.exit(1);
		}

		for(i=0;i<=max_index;i++)
		{
			feature_max[i] = -Double.MAX_VALUE;
			feature_min[i] = Double.MAX_VALUE;
		}

		fp = rewind(fp, data_filename);

		/* pass 2: find out min/max value */
		while(readline(fp) != null)
		{
			int next_index = 1;
			double target;
			double value;

			StringTokenizer st = new StringTokenizer(line,GlobalProperties.getIntlString("_\t\n\r\f:"));
			target = Double.parseDouble(st.nextToken());
			y_max = Math.max(y_max, target);
			y_min = Math.min(y_min, target);

			while (st.hasMoreTokens())
			{
				index = Integer.parseInt(st.nextToken());
				value = Double.parseDouble(st.nextToken());

				for (i = next_index; i<index; i++)
				{
					feature_max[i] = Math.max(feature_max[i], 0);
					feature_min[i] = Math.min(feature_min[i], 0);
				}

				feature_max[index] = Math.max(feature_max[index], value);
				feature_min[index] = Math.min(feature_min[index], value);
				next_index = index + 1;
			}

			for(i=next_index;i<=max_index;i++)
			{
				feature_max[i] = Math.max(feature_max[i], 0);
				feature_min[i] = Math.min(feature_min[i], 0);
			}
		}

		fp = rewind(fp, data_filename);

		/* pass 2.5: save/restore feature_min/feature_max */
		if(restore_filename != null)
		{
			// fp_restore rewinded in finding max_index 
			int idx, c;
			double fmin, fmax;

			fp_restore.mark(2);				// for reset
			if((c = fp_restore.read()) == 'y')
			{
				fp_restore.readLine();		// pass the '\n' after 'y'
				StringTokenizer st = new StringTokenizer(fp_restore.readLine());
				y_lower = Double.parseDouble(st.nextToken());
				y_upper = Double.parseDouble(st.nextToken());
				st = new StringTokenizer(fp_restore.readLine());
				y_min = Double.parseDouble(st.nextToken());
				y_max = Double.parseDouble(st.nextToken());
				y_scaling = true;
			}
			else
				fp_restore.reset();

			if(fp_restore.read() == 'x') {
				fp_restore.readLine();		// pass the '\n' after 'x'
				StringTokenizer st = new StringTokenizer(fp_restore.readLine());
				lower = Double.parseDouble(st.nextToken());
				upper = Double.parseDouble(st.nextToken());
				String restore_line = null;
				while((restore_line = fp_restore.readLine())!=null)
				{
					StringTokenizer st2 = new StringTokenizer(restore_line);
					idx = Integer.parseInt(st2.nextToken());
					fmin = Double.parseDouble(st2.nextToken());
					fmax = Double.parseDouble(st2.nextToken());
					if (idx <= max_index)
					{
						feature_min[idx] = fmin;
						feature_max[idx] = fmax;
					}
				}
			}
			fp_restore.close();
		}

		if(save_filename != null)
		{
			Formatter formatter = new Formatter(new StringBuilder());
			BufferedWriter fp_save = null;

			try {
				fp_save = new BufferedWriter(new FileWriter(save_filename));
			} catch(IOException e) {
				System.err.println(GlobalProperties.getIntlString("can't_open_file_") + save_filename);
				System.exit(1);
			}

			if(y_scaling)
			{
				formatter.format(GlobalProperties.getIntlString("y\n"));
				formatter.format(GlobalProperties.getIntlString("%.16g_%.16g\n"), y_lower, y_upper);
				formatter.format(GlobalProperties.getIntlString("%.16g_%.16g\n"), y_min, y_max);
			}
			formatter.format(GlobalProperties.getIntlString("x\n"));
			formatter.format(GlobalProperties.getIntlString("%.16g_%.16g\n"), lower, upper);
			for(i=1;i<=max_index;i++)
			{
				if(feature_min[i] != feature_max[i]) 
					formatter.format(GlobalProperties.getIntlString("%d_%.16g_%.16g\n"), i, feature_min[i], feature_max[i]);
			}
			fp_save.write(formatter.toString());
			fp_save.close();
		}

		/* pass 3: scale */
		while(readline(fp) != null)
		{
			int next_index = 1;
			double target;
			double value;

			StringTokenizer st = new StringTokenizer(line,GlobalProperties.getIntlString("_\t\n\r\f:"));
			target = Double.parseDouble(st.nextToken());
			output_target(target);
			while(st.hasMoreElements())
			{
				index = Integer.parseInt(st.nextToken());
				value = Double.parseDouble(st.nextToken());
				for (i = next_index; i<index; i++)
					output(i, 0);
				output(index, value);
				next_index = index + 1;
			}

			for(i=next_index;i<= max_index;i++)
				output(i, 0);
			System.out.print(GlobalProperties.getIntlString("\n"));
		}
		if (new_num_nonzeros > num_nonzeros)
			System.err.print(
			 GlobalProperties.getIntlString("Warning:_original_#nonzeros_") + num_nonzeros+GlobalProperties.getIntlString("\n")
			+GlobalProperties.getIntlString("_________new______#nonzeros_") + new_num_nonzeros+GlobalProperties.getIntlString("\n")
			+GlobalProperties.getIntlString("Use_-l_0_if_many_original_feature_values_are_zeros\n"));

		fp.close();
	}

	public static void main(String argv[]) throws IOException
	{
		SVMScale s = new SVMScale();
		s.run(argv);
	}
}