import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {	
		String usuarioDigitado = "";
		String senhaDigitada = "";
		String usuarioAutenticacao = "";
		String senhaAutenticacao = "";
		
		int option;
		
		Scanner lerUsuario = new Scanner(System.in);
		
		System.out.printf("\n###### MENU ######\n");
		System.out.printf("Escolha uma opcao\n");
		System.out.printf("1- Cadastre-se\n");
		System.out.printf("2- Login\n");
		option = lerUsuario.nextInt();
		lerUsuario.nextLine();
		
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
			
		FileWriter arq = new FileWriter("/Users/procob/eclipse-workspace/CriptografiaEx3/autenticacao.txt", true);
		PrintWriter gravarArq = new PrintWriter(arq);
		FileReader reader = new FileReader("/Users/procob/eclipse-workspace/CriptografiaEx3/autenticacao.txt");
		BufferedReader leitor = new BufferedReader(reader);
	
			
			switch (option) {
				case 1 :
					System.out.printf("\n###### CADASTRE-SE ######\n");
					
					System.out.printf("\nInforme seu usuario (4 caracteres): \n");
					usuarioDigitado = lerUsuario.nextLine();
					
					if(usuarioDigitado.length() != 4) {
						do {
							System.out.printf("\nO usuario deve conter 4 caracteres, tente novamente: \n");
							usuarioDigitado = lerUsuario.nextLine();
						}while(usuarioDigitado.length() != 4);
					}
						  
					System.out.printf("Informe sua senha (4 caracteres): \n");
					senhaDigitada = lerUsuario.nextLine();
					
					if(senhaDigitada.length() != 4) {
						do {
							System.out.printf("\nA senha deve conter 4 caracteres, tente novamente: \n");
							senhaDigitada = lerUsuario.nextLine();
						}while(senhaDigitada.length() != 4);
					}
					
					lerUsuario.close();
					
					 m.update(senhaDigitada.getBytes(),0,senhaDigitada.length());
					 String hash = new BigInteger(1,m.digest()).toString(16);
					 
					 System.out.printf("MD5: %s\n", hash);
					 
					 gravarArq.printf("%s, %s, %d\n", usuarioDigitado, hash, 0);
					 
					 arq.close();
					 leitor.close();
					 
					 System.out.print("\nUsu??rio Registrado com sucesso!\n");
					    
					break;
					
				case 2:
				
					System.out.printf("\n###### EFETUE LOGIN ######\n");
					
					System.out.printf("\nInforme seu usuario: \n");
					usuarioAutenticacao = lerUsuario.nextLine();
							  
					System.out.printf("Informe sua senha: \n");
					senhaAutenticacao = lerUsuario.nextLine();
					
					lerUsuario.close();
					
				    m.update(senhaAutenticacao.getBytes(),0,senhaAutenticacao.length());
				    String hash2 = new BigInteger(1,m.digest()).toString(16);

					String linha = "";
					String[] userFound = null;
					boolean credenciaisIncorretas = false;
					int tentativas = 0;
					boolean userBlocked = false;
					do {
						linha = leitor.readLine();
						if(linha.contains(usuarioAutenticacao+",") == true) {
							userFound = linha.split(", ");
							tentativas = Integer.parseInt(userFound[2]);
							if(tentativas >= 5) {
								userBlocked = true;
								System.out.print("\n\nNUMERO DE TENTATIVAS EXCEDIDO, USU??RIO BLOQUEADO");
							}else {
								if(usuarioAutenticacao.equals(userFound[0]) && hash2.equals(userFound[1])) {
									System.out.print("\n\nCREDENCIAIS CORRETAS");
									credenciaisIncorretas = false;
								}else {
									credenciaisIncorretas = true;
									if(tentativas >= 4) {
										System.out.print("\n\nNUMERO DE TENTATIVAS EXCEDIDO, USU??RIO BLOQUEADO");
									}else {
										System.out.print("\n\nCREDENCIAIS INCORRETAS");
									}
								}
							}
		
							break;
						}
					}while((linha = leitor.readLine()) != null);
					
					arq.close();
					gravarArq.close();
					
					
					if(linha != null && userBlocked == false) {
						FileWriter arq2 = new FileWriter("/Users/procob/eclipse-workspace/CriptografiaEx3/autenticacao.txt");
						PrintWriter gravarArq2 = new PrintWriter(arq2);
						
						String l = leitor.readLine();
						ArrayList<String> salvar = new ArrayList<String>();
						
						while(l != null) {
							if(l.contains(usuarioAutenticacao+",") == false) {
								salvar.add(l+"\n");
							}
							
							l = leitor.readLine();
						}
						if(credenciaisIncorretas == true) {
							int tentativasAtualizadas = tentativas+1;
							String novaLinha = userFound[0]+ ", "+userFound[1]+", "+tentativasAtualizadas+"\n";
							salvar.add(novaLinha);
							
						}else {
							String novaLinha = userFound[0]+ ", "+userFound[1]+", "+0+"\n";
							salvar.add(novaLinha);
						}
						
						for(int i = 0; i<salvar.size(); i++) {
							gravarArq2.print(salvar.get(i));
						}
						
						arq2.close();
						gravarArq2.close();
					
					}
					
					leitor.close();
					break;
					
			}
		   
	}
}