import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
public class ca1 {

	public static void main(String[] args){
		// TODO Auto-generated method stub
		File file=new File("C:\\sample.txt");
		FileReader filereader = null;
		try {
			filereader = new FileReader(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader bufreader=new BufferedReader(filereader);
		
		Vector<Vector <String>> row = new Vector();
		
		Vector nonterminal=new Vector();
		Vector terminal=new Vector();
		String line="";
		String first="";
		int k=0;
		try {
			while((line=bufreader.readLine())!=null) {
				Vector<String> col = new Vector<String>();
				String words[]=line.split(" ");
				int check=0;
				boolean ifor=false;
				for(String wo:words) {
					if(wo.equals("==>")) {check++;}
					else if(wo.equals("||")) {col.add("OR");if(check==0) {ifor=true;check++;}} 
					else if(wo.equals("__")) {
						col.add("zero");
						if(nonterminal.contains("zero")) {continue;}
						nonterminal.add("zero");
					}//입실론
					else if(wo.compareTo("A")>0&&wo.compareTo("Z")<0) {
						if(check==0) {
							check++;
							first=wo;
						}
						else {col.add(wo);}
						if(nonterminal.contains(wo)) {continue;}
						nonterminal.add(wo);
					}
					else {
						col.add(wo);
						if(terminal.contains(wo)) {continue;}
						terminal.add(wo);
					}
				}
				k++;
				col.add(0, first);
				if(ifor) {
					String start=row.get(k-1).get(0);
					Vector v=new Vector();
					v.add(start);
					for(int i=0;i<col.size();i++) {v.add(col.get(i));}
					row.add(v);
				}
				else{row.add(col);}
				first="";
			}
			bufreader.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}/*
		for(int i=0;i<row.size();i++){System.out.println(row.get(i));}
		for(int i=0;i<terminal.size();i++) {System.out.print(terminal.get(i)+" ");}   //터미널과 논터미널 분류, 이어지는거 구분
		System.out.println();
		for(int i=0;i<nonterminal.size();i++) {System.out.print(nonterminal.get(i)+" ");}*/
		//First 구하기
		Vector<Vector <String>> First=new Vector();
		for(int i=0;i<row.size();i++) {//first 구하기 1단계
			Vector<String> v=new Vector();
			String isfirst=row.get(i).get(0);
			v.add(isfirst);
			First.add(v);
			if(terminal.contains(isfirst)) {
				Vector v2=new Vector();
				v2.add(isfirst);
				First.add(i,v2);
			}
		}
		//for(int i=0;i<First.size();i++){System.out.println(First.get(i)+" ");}
		for(int i=0;i<row.size();i++) {//first 구하기 2단계
			String isfirst=First.get(i).get(0);
			if(nonterminal.contains(isfirst)) {
				String s=row.get(i).get(1);
				if(terminal.contains(s)) {
					Vector v=new Vector();
					for(int j=0;j<First.get(i).size();j++) {
						v.add(First.get(i).get(j));
					}
					if(v.contains(s)) {continue;}
					v.add(s);
					First.remove(i);
					First.add(i, v);
				}
			}
		}
		//for(int i=0;i<First.size();i++){System.out.println(First.get(i));}
		for(int i=0;i<row.size();i++) {//first 구하기 3단계
			Vector v=new Vector();
			for(int j=0;j<First.get(i).size();j++) {
				v.add(First.get(i).get(j));
			}
			if(row.get(i).contains("zero")) {
				if(v.contains("zero")) {continue;}
				v.add("zero");
			}
			First.remove(i);
			First.add(i,v);
		}
		for(int i=0;i<row.size();i++) {//first 구하기 4단계
			String start=row.get(i).get(0);
			String s=row.get(i).get(1);
			if(start.equals(s)) {continue;}
			Vector v=new Vector();
			for(int l=0;l<First.get(i).size();l++) {
				v.add(First.get(i).get(l));
			}
			for(int j=i+1;j<row.size();j++) {
				String s2=row.get(j).get(0);
				if(s2.equals(s)&&s.equals(row.get(j).get(1))==false) {
					String s3=row.get(j).get(1);
					if(nonterminal.contains(s3)) {s=s3;}
					if(terminal.contains(s3)) {
						if(v.contains(s3)) {continue;}
						v.add(s3);
					}
				}		
			}
			First.remove(i);
			First.add(i,v);
		}
		//for(int i=0;i<First.size();i++){System.out.println(First.get(i));}
		for(int i=0;i<First.size();i++) {//first에서 반복되는 열 합치기
			String s=First.get(i).get(0);
			int len1=First.get(i).size();
			int len2=0;
			for(int j=i+1;j<First.size();j++) {
				if(s.equals(First.get(j).get(0))) {
					len2=First.get(j).size();
					if(len1>len2) {
						Vector v=new Vector();
						for(int l=0;l<len1;l++) {
							v.add(First.get(i).get(l));
						}
						for(int l=1;l<len2;l++) {
							if(First.get(i).contains(First.get(j).get(l))) {continue;}
							v.add(First.get(j).get(l));
						}
						First.remove(j);
						First.remove(i);
						First.add(i,v);
					}
					else {
						Vector v=new Vector();
						for(int l=0;l<len2;l++) {
							v.add(First.get(j).get(l));
						}
						for(int l=1;l<len1;l++) {
							if(First.get(j).contains(First.get(i).get(l))) {continue;}
							v.add(First.get(i).get(l));
						}
						First.remove(j);
						First.remove(i);
						First.add(i,v);
					}
				}
			}
		}
		//for(int i=0;i<First.size();i++){System.out.println(First.get(i));}
		//팔로우 구하기
		Vector<Vector <String>> Follow=new Vector();
		for(int i=0;i<First.size();i++) {//Follow 구하기 1단계
			Vector v=new Vector();
			v.add(First.get(i).get(0));
			if(i==0) {v.add("$");}
			Follow.add(v);
		}
		//for(int i=0;i<Follow.size();i++){System.out.println(Follow.get(i));}
		for(int i=0;i<First.size();i++) {//Follow 구하기 2단계
			String s=Follow.get(i).get(0);
			Vector v=new Vector();
			for(int j=0;j<Follow.get(i).size();j++) {v.add(Follow.get(i).get(j));}
			for(int j=0;j<row.size();j++) {
				for(int l=1;l<row.get(j).size()-1;l++) {
					if(s.equals(row.get(j).get(l))) {
						if(terminal.contains(row.get(j).get(l+1))) {
							if(v.contains(row.get(j).get(l+1))) {continue;}
							v.add(row.get(j).get(l+1));
						}
					}
				}
				for(int l=1;l<row.get(j).size()-1;l++) {
					if(s.equals(row.get(j).get(l))) {
						if(nonterminal.contains(row.get(j).get(l+1))) {
							String s2=row.get(j).get(l+1);
							for(int n=0;n<First.size();n++) {
								if(s.equals(First.get(n).get(0))) {
									if(v.contains(First.get(n).get(1))) {continue;}
									v.add(First.get(n).get(1));
								}
							}
						}
					}
				}
			}
			Follow.remove(i);
			Follow.add(i, v);
		}
		//for(int i=0;i<Follow.size();i++){System.out.println(Follow.get(i));}
		for(int i=0;i<First.size();i++) {//Follow 구하기 3단계
			String s=Follow.get(i).get(0);
			for(int j=0;j<row.size();j++) {
				if(s.equals(row.get(j).get(0))) {
					if(!s.equals(row.get(j).get(1))&&nonterminal.contains(row.get(j).get(1))) {
						Vector v=new Vector();
						int pos=0;
						String s2=row.get(j).get(1);
						for(int n=0;n<Follow.size();n++) {
							if(s2.equals(Follow.get(n).get(0))) {pos=n;}
						}
						for(int n=0;n<Follow.get(pos).size();n++) {
							v.add(Follow.get(pos).get(n));
						}
						for(int n=1;n<Follow.get(i).size();n++) {
							if(Follow.get(pos).contains(Follow.get(i).get(n))) {continue;}
							v.add(Follow.get(i).get(n));
						}
						Follow.remove(pos);
						Follow.add(pos, v);
					}
				}
			}
		}
		//for(int i=0;i<Follow.size();i++){System.out.println(Follow.get(i));}
		Vector<Vector <String>> splus=new Vector();
		for(int i=0;i<row.size();i++){//증가 문법 만들기
			Vector v=new Vector();
			if(i==0) {
				Vector v2=new Vector();
				v2.add("S");
				v2.add(row.get(0).get(0));
				splus.add(v2);
			}
			for(int j=0;j<row.get(i).size();j++) {v.add(row.get(i).get(j));}
			splus.add(v);
		}
		//for(int i=0;i<splus.size();i++){System.out.println(splus.get(i));}
		Vector<Vector <Vector <String>>> I=new Vector();
		//·
		Vector match=new Vector();
		for(int i=0;i<splus.size();i++) {//I0
			Vector v=new Vector();
			String start = null;
			if(i==0) {
				v.add(splus.get(i).get(0));
				v.add("·");
				for(int j=1;j<splus.get(i).size();j++) {
					if(j==1) {start=splus.get(i).get(j);}
					v.add(splus.get(i).get(j));			
				}
				splus.remove(i);
				splus.add(i, v);
			}
			else {
				int m=(int)match.get(0);
				v.add(splus.get(m).get(0));
				v.add("·");
				for(int j=1;j<splus.get(m).size();j++) {
					if(j==1) {start=splus.get(m).get(j);}
					v.add(splus.get(m).get(j));			
				}
				match.remove(0);
				splus.remove(m);
				splus.add(m, v);
			}
			if(i==0) {
				for(int j=0;j<splus.size();j++) {
					if(start.equals(splus.get(j).get(0))) {match.add(j);}
				}
			}
			else {
				if(match.size()==0) {
					for(int j=0;j<splus.size();j++) {
						if(start.equals(splus.get(j).get(0))) {match.add(j);}
					}
				}
			}
		}
		//for(int i=0;i<splus.size();i++){System.out.println(splus.get(i));}
		I.add(splus);
		//System.out.println(I.get(0));
		for(int i=0;i<nonterminal.size();i++) {//논터미널꺼 추가
			String find=(String) nonterminal.get(i);
			Vector<Vector <String>> temp=new Vector();
			Vector v=new Vector();
			v.add(0);
			v.add(find);
			temp.add(v);
			for(int j=0;j<splus.size();j++) {
				int findindex = 0;
				if(splus.get(j).contains(find)) {
					for(int m=0;m<splus.get(j).size();m++) {
						if(find.equals(splus.get(j).get(m))&&m!=0) {
							findindex=m;
						}
					}
					if(findindex!=0) {
						if(splus.get(j).get(findindex-1).equals("·")){
							Vector v2=new Vector();
							for(int m=0;m<findindex-1;m++) {
								v2.add(splus.get(j).get(m));
							}
							v2.add(find);
							v2.add("·");
							for(int m=findindex+1;m<splus.get(j).size();m++) {
								v2.add(splus.get(j).get(m));
							}
							temp.add(v2);
							if(findindex!=splus.get(j).size()-1) {
								for(int m=0;m<nonterminal.size();m++) {
									String nonter=(String) nonterminal.get(m);
									for(int a=1;a<temp.size();a++) {
										int out=0;
										for(int b=1;b<temp.get(a).size();b++) {			
											if(nonter.equals(temp.get(a).get(b))&&temp.get(a).get(b-1).equals("·")) {
												for(int n=0;n<splus.size();n++) {
													if(nonter.equals(splus.get(n).get(0))) {
														out=1;
														temp.add(splus.get(n));
													}
												}
											}
										}
										if(out==1) {break;}
									}
								}
							}
						}
					}
				}
			}
			if(temp.size()>1) {I.add(temp);}
		}
		//for(int i=0;i<I.size();i++) {System.out.println(I.get(i));}
		
		for(int i=0;i<terminal.size();i++) {//터미널꺼 추가
			String find=(String) terminal.get(i);
			Vector<Vector <String>> temp=new Vector();
			Vector v=new Vector();
			v.add(0);
			v.add(find);
			temp.add(v);
			for(int j=0;j<splus.size();j++) {
				int findindex = 0;
				if(splus.get(j).contains(find)) {
					for(int m=0;m<splus.get(j).size();m++) {
						if(find.equals(splus.get(j).get(m))&&m!=0) {
							findindex=m;
						}
					}
					if(findindex!=0) {
						if(splus.get(j).get(findindex-1).equals("·")){
							Vector v2=new Vector();
							for(int m=0;m<findindex-1;m++) {
								v2.add(splus.get(j).get(m));
							}
							v2.add(find);
							v2.add("·");
							for(int m=findindex+1;m<splus.get(j).size();m++) {
								v2.add(splus.get(j).get(m));
							}
							temp.add(v2);
							if(findindex!=splus.get(j).size()-1) {
								for(int m=0;m<nonterminal.size();m++) {
									String nonter=(String) nonterminal.get(m);
									for(int a=1;a<temp.size();a++) {
										int out=0;
										for(int b=1;b<temp.get(a).size();b++) {			
											if(nonter.equals(temp.get(a).get(b))&&temp.get(a).get(b-1).equals("·")) {
												for(int n=0;n<splus.size();n++) {
													if(nonter.equals(splus.get(n).get(0))) {
														out=1;
														temp.add(splus.get(n));
													}
												}
											}
										}
										if(out==1) {break;}
									}
								}
							}
						}
					}
				}
			}
			if(temp.size()>1) {I.add(temp);}
		}
		//for(int i=0;i<I.size();i++) {System.out.println(I.get(i));}
		
		for(int i=1;i<I.size();i++) {
			Vector<Vector <String>> receiver=new Vector();
			receiver=I.get(i);
			for(int j=1;j<receiver.size();j++) {
				Vector<Vector <String>> temp=new Vector();
				Vector startv=new Vector();
				int dotpos=receiver.get(j).indexOf("·");
				if(dotpos==receiver.get(j).size()-1) {continue;}
				String dotnextc=receiver.get(j).get(dotpos+1);
				startv.add(i);//i번째 I로부터의 분기
				startv.add(dotnextc);
				temp.add(startv);
				Vector v=new Vector();
				for(int l=0;l<dotpos;l++) {
					v.add(receiver.get(j).get(l));
				}
				v.add(dotnextc);
				v.add("·");
				for(int l=dotpos+2;l<receiver.get(j).size();l++) {
					v.add(receiver.get(j).get(l));
				}
				temp.add(v);
				if(dotpos+2<receiver.get(j).size()) {
					String find=receiver.get(j).get(dotpos+2);
				}
				if(dotpos<receiver.get(j).size()-2) {
					for(int m=0;m<nonterminal.size();m++) {
						String nonter=(String) nonterminal.get(m);
						for(int a=1;a<temp.size();a++) {
							int out=0;
							for(int b=1;b<temp.get(a).size();b++) {			
								if(nonter.equals(temp.get(a).get(b))&&String.valueOf(temp.get(a).get(b-1)).equals("·")) {
									for(int n=0;n<splus.size();n++) {
										if(nonter.equals(splus.get(n).get(0))) {
											out=1;
											temp.add(splus.get(n));
										}
									}
								}
							}
							if(out==1) {break;}
						}
					}
				}
				int check2=0;
				int checkl=0;
				for(int l=1;l<I.size();l++) {
					if(I.get(l).get(0).equals(temp.get(0))) {
						check2++;
						checkl=l;
						for(int m=1;m<temp.size();m++) {
							if(I.get(l).contains(temp.get(m))) {continue;}
							I.get(l).add(temp.get(m));
						}
						for(int m=1;m<I.size()-1;m++) {
							int count=0;
							if(I.get(m).size()==I.get(checkl).size()) {
								for(int n=1;n<I.get(checkl).size();n++) {
									if(I.get(m).get(n).equals(I.get(checkl).get(n))) {count++;}
								}
							}
							if(count==I.get(checkl).size()-1) {//같은 거 가리킬 경우에 굳이 추가하지 않음
								I.get(m).get(0).add(String.valueOf(I.get(checkl).get(0).get(0)));
								I.get(m).get(0).add(I.get(checkl).get(0).get(1));
								I.remove(checkl);
								break;
							}
						}
						if(check2!=0) {break;}
					}
				}
				if(check2!=0) {continue;}
				int check=0;
				for(int l=1;l<I.size()-1;l++) {
					int count=0;
					if(I.get(l).size()==temp.size()) {
						for(int m=1;m<I.get(l).size();m++) {
							if(I.get(l).get(m).equals(temp.get(m))) {count++;}
						}
					}
					if(count==temp.size()-1) {//같은 거 가리킬 경우에 굳이 추가하지 않음
						check++;
						I.get(l).get(0).add(String.valueOf(temp.get(0).get(0)));
						I.get(l).get(0).add(temp.get(0).get(1));
						break;
					}
				}
				if(check==0) {I.add(temp);}
			}
		}
		//for(int i=0;i<I.size();i++) {System.out.println(I.get(i));}

		String SLR[][]=new String[I.size()+1][nonterminal.size()+terminal.size()+2];
		for(int i=0;i<I.size()+1;i++) {
			for(int j=0;j<nonterminal.size()+terminal.size()+2;j++) {
				SLR[i][j]=" ";
			}
		}
		for(int i=1;i<I.size()+1;i++) {SLR[i][0]=String.valueOf(i-1);}
		for(int i=1;i<terminal.size()+1;i++) {SLR[0][i]=(String) terminal.get(i-1);}
		SLR[0][terminal.size()+1]="$";
		for(int i=0;i<nonterminal.size();i++) {SLR[0][i+terminal.size()+2]=(String) nonterminal.get(i);}
		
		for(int i=1;i<I.size()+1;i++) {//배열 행만큼
			for(int j=1;j<I.size();j++) {//I 크기만큼
				if(I.get(j).get(0).contains(Integer.parseInt(SLR[i][0]))||I.get(j).get(0).contains(String.valueOf(SLR[i][0]))) {
					int pos=-1;
					pos=I.get(j).get(0).indexOf(Integer.parseInt(SLR[i][0]));
					if(pos==-1) {pos=I.get(j).get(0).indexOf(String.valueOf(SLR[i][0]));}
					for(int l=0;l<terminal.size();l++) {//터미널인경우 s
						String shift="s";//이동
						if(String.valueOf(I.get(j).get(0).get(pos+1)).equals(String.valueOf(SLR[0][l+1]))) {
							shift=shift+String.valueOf(j);
							SLR[i][l+1]=shift;
						}
					}
					for(int l=0;l<nonterminal.size();l++) {//논터미널인경우
						if(String.valueOf(I.get(j).get(0).get(pos+1)).equals(String.valueOf(SLR[0][terminal.size()+l+2]))) {
							SLR[i][terminal.size()+l+2]=String.valueOf(j);
						}
					}
				}
			}
			if(i==I.size()) {break;}
			for(int j=1;j<I.get(i).size();j++) {//감축
				int dotpos=I.get(i).get(j).indexOf("·");
				if(dotpos==I.get(i).get(j).size()-1) {
					String firstc=I.get(i).get(j).get(0);
					Vector v=new Vector();
					for(int m=0;m<dotpos;m++) {
						v.add(I.get(i).get(j).get(m));
					}
					v.add(1, "·");
					String reduce="r";
					if(v.equals(I.get(0).get(0))) {
						reduce="acc";
						SLR[i+1][terminal.size()+1]=reduce;
						break;
					}
					else {
						for(int m=1;m<I.get(0).size();m++) {
							if(v.equals(I.get(0).get(m))) {
								reduce=reduce+String.valueOf(m);
								break;
							}
						}
					}
					for(int m=0;m<Follow.size();m++) {
						if(Follow.get(m).get(0).equals(firstc)) {
							for(int n=0;n<Follow.get(m).size();n++) {
								for(int p=0;p<terminal.size()+1;p++) {
									if(Follow.get(m).get(n).equals(SLR[0][1+p])) {
										SLR[i+1][p+1]=reduce;
										break;
									}
								}
							}
						}
					}
				}
			}
		}/*
		for(int i=0;i<I.size()+1;i++) {
			for(int j=0;j<nonterminal.size()+terminal.size()+2;j++) {
				System.out.print(SLR[i][j]+" ");
			}
			System.out.println();
		}*/
		File file2=new File("C:\\example.txt");//구문분석할꺼 읽어옴
		FileReader filereader2 = null;
		Vector parsen=new Vector();
		try {
			filereader2 = new FileReader(file2);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader bufreader2=new BufferedReader(filereader2);
		try {
			while((line=bufreader2.readLine())!=null) {
				String words[]=line.split(" ");
				for(String wo:words) {
					parsen.add(wo);
				}
			}
			bufreader2.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String stack[][]=new String[300][3];//구문분석표
		Vector stk=new Vector();
		stk.add("0");
		for(int i=0;;i++) {
			if(i==0) {
				String str="";
				String str2="";
				for(int j=0;j<stk.size();j++) {
					str2=str2+stk.get(j);
				}
				stack[0][0]=str2;
				for(int j=0;j<parsen.size();j++) {
					str=str+parsen.get(j);
				}
				if(parsen.size()==0) {parsen.add("$");}
				str=str+"$";
				stack[0][1]=str;
				for(int j=1;j<nonterminal.size()+terminal.size()+2;j++) {//구문분석내용-이동
					if(SLR[0][j].equals(parsen.get(0))) {
						stack[0][2]=SLR[1][j];
						String start=SLR[1][j];
						stk.add(parsen.get(0));
						stk.add(start.charAt(1));
						parsen.remove(0);
						break;
					}
				}
			}
			else {
				String str2="";
				for(int j=0;j<stk.size();j++) {
					str2=str2+stk.get(j);
				}
				stack[i][0]=str2;
				String str="";
				if(parsen.size()==0) {parsen.add("$");}
				for(int j=0;j<parsen.size();j++) {
					str=str+parsen.get(j);
				}
				if(parsen.size()>1||parsen.get(0)!="$") {str=str+"$";}
				stack[i][1]=str;
				String last=String.valueOf(stk.lastElement());	
				if(!terminal.contains(last)&&!nonterminal.contains(last)) {//감축 or 이동
					for(int j=1;j<nonterminal.size()+terminal.size()+1;j++) {
						if(parsen.get(0).equals(SLR[0][j])) {
							stack[i][2]=SLR[Integer.parseInt(last)+1][j];
							String content=stack[i][2];
							if(content.charAt(0)=='s') {//이동
								stk.add(parsen.get(0));
								stk.add(content.substring(1));
								parsen.remove(0);
							}
							else if(content.charAt(0)=='r') {//감축
								int n=Integer.parseInt(last);
								for(int l=1;l<I.get(n).size();l++) {
									if(I.get(n).get(l).indexOf("·")==I.get(n).get(l).size()-1) {
										int count=0;
										String s=String.valueOf(stk.lastElement());
										int num=Integer.parseInt(s);
										for(int m=1;m<I.get(num).size();m++) {
											if(I.get(num).get(m).get(0).equals(I.get(n).get(l).get(0))) {
												count=I.get(num).get(m).size()-2;
												break;
											}
										}
										for(int m=0;m<2*count;m++) {					
											stk.remove(stk.size()-1);
										}
										stk.add(I.get(n).get(l).get(0));
										break;
									}
								}
							}
							break;
						}
					}
				}
				else if(nonterminal.contains(last)) {//goto
					String lasttwo=String.valueOf(stk.get(stk.size()-2));
					for(int j=1;j<I.size();j++) {
						if(I.get(j).get(0).contains(lasttwo)||I.get(j).get(0).contains(Integer.parseInt(lasttwo))) {
							int npos=-1;
							npos=I.get(j).get(0).indexOf(lasttwo);
							if(npos==-1) {npos=I.get(j).get(0).indexOf(Integer.parseInt(lasttwo));}
							if(I.get(j).get(0).get(npos+1).equals(stk.lastElement())) {
								stack[i][2]="goto"+String.valueOf(j);
								stk.add(String.valueOf(j));
								break;
							}
						}
					}
				}
			}
			//System.out.println(i+" "+stack[i][0]+" "+stack[i][1]+" "+stack[i][2]);//구문분석 출력
			if(stack[i][2].equals("acc")) {break;}
		}
		Vector<Vector <String>> cplus=new Vector();//CLR 0번 될꺼
		for(int i=0;i<row.size();i++){//증가 문법 만들기
			Vector v=new Vector();
			if(i==0) {
				Vector v2=new Vector();
				v2.add("E'");
				v2.add(row.get(0).get(0));
				cplus.add(v2);
			}
			for(int j=0;j<row.get(i).size();j++) {v.add(row.get(i).get(j));}
			cplus.add(v);
		}
		//for(int i=0;i<cplus.size();i++) {System.out.println(cplus.get(i));}
		
		Vector<Vector <Vector <String>>> CI=new Vector();
		Vector match2=new Vector();
		for(int i=0;i<cplus.size();i++) {//CI 0번
			Vector v=new Vector();
			String start = null;
			if(i==0) {
				v.add(cplus.get(i).get(0));
				v.add("·");
				for(int j=1;j<cplus.get(i).size();j++) {
					if(j==1) {start=cplus.get(i).get(j);}
					v.add(cplus.get(i).get(j));			
				}
				v.add(",");
				v.add("$");
				cplus.remove(i);
				cplus.add(i, v);
			}
			else {
				int m=(int)match2.get(0);
				v.add(cplus.get(m).get(0));
				v.add("·");
				for(int j=1;j<cplus.get(m).size();j++) {
					if(j==1) {start=cplus.get(m).get(j);}
					v.add(cplus.get(m).get(j));			
				}
				v.add(",");
				for(int j=0;j<m;j++) {
					if(cplus.get(j).contains(cplus.get(m).get(0))) {
						int startpos=0;
						for(int l=1;l<cplus.get(j).size();l++) {
							if(cplus.get(j).get(l).equals(cplus.get(m).get(0))) {startpos=l;break;}
						}
						if(startpos<1) {continue;}
						if(cplus.get(j).get(startpos-1).equals("·")) {
							int pos=cplus.get(j).indexOf(",");
							for(int l=pos+1;l<cplus.get(j).size();l++) {
								if(!v.contains(cplus.get(j).get(l))) {v.add(cplus.get(j).get(l));}
							}
						}
					}
				}
				if(cplus.get(m).size()>3) {
					if(terminal.contains(cplus.get(m).get(3))) {
						int check=0;
						int apos=v.indexOf(",");
						for(int j=apos+1;j<v.size();j++) {
							if(v.get(j).equals(cplus.get(m).get(3))) {check++;}
						}
						if(check==0) {v.add(cplus.get(m).get(3));}
					}
				}
				if(cplus.get(m).size()>2) {
					if(terminal.contains(cplus.get(m).get(2))) {
						int check=0;
						int apos=v.indexOf(",");
						for(int j=apos+1;j<v.size();j++) {
							if(v.get(j).equals(cplus.get(m).get(2))) {check++;}
						}
						if(check==0) {v.add(cplus.get(m).get(2));}
					}
					if(nonterminal.contains(cplus.get(m).get(2))&&nonterminal.contains(cplus.get(m).get(1))) {
						for(int j=0;j<First.size();j++) {
							if(First.get(j).get(0).equals(cplus.get(m).get(2))) {
								for(int l=1;l<First.get(j).size();l++) {									
									int check=0;
									int apos=v.indexOf(",");
									for(int n=apos+1;n<v.size();n++) {
										if(v.get(n).equals(cplus.get(j).get(l))) {check++;}
									}
									if(check==0) {v.add(First.get(j).get(l));}
								}
							}
						}
					}
				}		
				match2.remove(0);
				cplus.remove(m);
				cplus.add(m, v);
			}
			if(i==0) {
				for(int j=0;j<cplus.size();j++) {
					if(start.equals(cplus.get(j).get(0))) {match2.add(j);}
				}
			}
			else {
				if(match2.size()==0) {
					for(int j=0;j<cplus.size();j++) {
						if(start.equals(cplus.get(j).get(0))) {match2.add(j);}
					}
				}
			}
		}
		for(int i=0;i<cplus.size();i++){System.out.println(cplus.get(i));}
		CI.add(cplus);
		
		for(int i=1;i<nonterminal.size();i++) {//CLR 논터미널 추가
			Vector<Vector <String>> receiver=new Vector();
			receiver=I.get(i);
		}
	}

}
