package wekaTeamCSV;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class csvRandomExtract {
	
	static List<List<String>> allData = new ArrayList<List<String>>(); // csv������ ������ ����Ʈ
	static List<List<String>> writeData = new ArrayList<List<String>>(); // ����� ������ ���� ����Ʈ
	
	static void randomWrite(int startSize, int finishSize, int startRecord, int finishRecord) {
		// random �迭 ����
		// record : �Է������� ���ڵ� �� ��ȣ
		int size = finishSize - startSize;
		int arrayR[] = new int[size];
		Random r = new Random();
		for (int i = 0; i < size; i++) {
			arrayR[i] = r.nextInt(finishRecord-startRecord) + startRecord;
			for (int j = 0; j < i; j++) {
				if (arrayR[i] == arrayR[j])
					i--;
			}
		}
		Arrays.sort(arrayR);
		
		// writeData ä���
		List<String> Line = new ArrayList<String>();
		int j = 0;
		for(int i = startRecord; i<finishRecord; i++) {
			if(i == arrayR[j]){
				Line = allData.get(i);
				List<String> writeLine = new ArrayList<String>();
				for (String data : Line) {
					writeLine.add(data);
				}
				writeData.add(startSize+j, writeLine);
				j++;
			}
			if (j == size) break;
		}
	}

	
	public static void main(String[] args) {
		
		BufferedReader br = null;
		
		//���ϴ� ��ŭ ����
		int inputSize = 3001;   //�Է������� ��ü ��
		int classIndex = 12;
		int classNum = 5;
		int OutputSize = 1280;   //����� �ν��Ͻ� ��
		String className = "price_class";
		int splitSize = OutputSize / classNum ;   //�� Ŭ�������� ����� �ν��Ͻ� ��
		
		
		List<Integer> classSplit = new ArrayList<Integer>(); //Ŭ���� ���� �޶����� ��
		
		// load CSV
		try {
			br = Files.newBufferedReader(
					Paths.get("C:\\Users\\dkdle\\Desktop\\�����ڷ�\\2020-2�б�\\�����͸��̴�\\�߰���\\�߰�������3000����.csv"));
			Charset.forName("UTF-8");
			String line = "";
			
			int i =0;
			
			// CSV������ �� ���� allData�� ����. Ŭ���� ���� �޶����� classSplit�� �� �� ����.
			while ((line = br.readLine()) != null) {
				i++;
				List<String> tmpList = new ArrayList<String>();
				String array[] = line.split(",");
				tmpList = Arrays.asList(array);
				if(!className.equals(tmpList.get(classIndex))) {
					className = tmpList.get(classIndex);
					classSplit.add(i);
				}
				//System.out.println(tmpList);
				allData.add(tmpList);
			}
			classSplit.add(inputSize+1); // ��������+1���� ����.
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// ����
		for(int i = 0; i<classNum; i++) {
			randomWrite(splitSize*i, splitSize*(i+1), classSplit.get(i), classSplit.get(i+1)-1);
		}
		
		
		// write CSV
		BufferedWriter bufWriter = null;
		try {
			bufWriter = Files.newBufferedWriter(Paths
					.get("C:\\Users\\dkdle\\Desktop\\�����ڷ�\\2020-2�б�\\�����͸��̴�\\�߰���\\�߰�������"+ OutputSize +"b.csv"),
					Charset.forName("UTF-8"));
			
			//bufWriter.write("manufacturer,type,size,year,condition,fuel,odometer,transmission,drive,cylinders,paint_color,price,price_class,"); 
			//bufWriter.newLine();
			
			for (List<String> newLine : writeData) {
				System.out.println(newLine);
				for (String data : newLine) {
					bufWriter.write(data);
					bufWriter.write(",");
				}
				bufWriter.newLine();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufWriter != null) {
					bufWriter.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
