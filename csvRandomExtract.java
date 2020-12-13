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
	
	static List<List<String>> allData = new ArrayList<List<String>>(); // csv원본을 저장할 리스트
	static List<List<String>> writeData = new ArrayList<List<String>>(); // 출력할 내용을 담은 리스트
	
	static void randomWrite(int startSize, int finishSize, int startRecord, int finishRecord) {
		// random 배열 생성
		// record : 입력파일의 레코드 행 번호
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
		
		// writeData 채우기
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
		
		//원하는 만큼 수정
		int inputSize = 3001;   //입력파일의 전체 행
		int classIndex = 12;
		int classNum = 5;
		int OutputSize = 1280;   //출력할 인스턴스 수
		String className = "price_class";
		int splitSize = OutputSize / classNum ;   //각 클래스마다 출력할 인스턴스 수
		
		
		List<Integer> classSplit = new ArrayList<Integer>(); //클래스 값이 달라지는 행
		
		// load CSV
		try {
			br = Files.newBufferedReader(
					Paths.get("C:\\Users\\dkdle\\Desktop\\공부자료\\2020-2학기\\데이터마이닝\\중고차\\중고차랜덤3000최종.csv"));
			Charset.forName("UTF-8");
			String line = "";
			
			int i =0;
			
			// CSV파일의 매 행을 allData에 저장. 클래스 값이 달라지면 classSplit에 행 값 저장.
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
			classSplit.add(inputSize+1); // 마지막행+1까지 저장.
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
		
		// 실행
		for(int i = 0; i<classNum; i++) {
			randomWrite(splitSize*i, splitSize*(i+1), classSplit.get(i), classSplit.get(i+1)-1);
		}
		
		
		// write CSV
		BufferedWriter bufWriter = null;
		try {
			bufWriter = Files.newBufferedWriter(Paths
					.get("C:\\Users\\dkdle\\Desktop\\공부자료\\2020-2학기\\데이터마이닝\\중고차\\중고차랜덤"+ OutputSize +"b.csv"),
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
