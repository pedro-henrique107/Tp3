package Tp3;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.*;

class ArquivoTextoLeitura {

	private BufferedReader entrada;
	
	ArquivoTextoLeitura(String nomeArquivo) {	
		
		try {
			entrada = new BufferedReader(new FileReader(nomeArquivo));
		}
		catch (FileNotFoundException excecao) {
			System.out.println("Arquivo não encontrado");
		}
	}
	
	public void fecharArquivo() {
		
		try {
			entrada.close();
		}
		catch (IOException excecao) {
			System.out.println("Erro no fechamento do arquivo de leitura: " + excecao);	
		}
	}
	
	@SuppressWarnings("finally")
	public String ler() {
		
		String textoEntrada = null;
		
		try {
			textoEntrada = entrada.readLine();
		}
		catch (EOFException excecao) { //Exceção de final de arquivo.
			textoEntrada = null;
		}
		catch (IOException excecao) {
			System.out.println("Erro de leitura: " + excecao);
			textoEntrada = null;
		}
		finally {
			return textoEntrada;
		}
	}
}

class PesquisaSequencial {

	public Musica buscarId(Musica[] listaMusicas, String id) {
		
		for (int i = 0; i < listaMusicas.length; i++)
			if (listaMusicas[i].getId().equals(id))
					return listaMusicas[i];
		
		return null;
	}	
}

class Musica {

	private String id;
	private String name;             // nome
	private String key;              // chave
	private String[] artists;        // lista de artistas
	private Date release_date;       // data de lançamento
	private double acousticness;     // acústica
	private double danceability;     // dança
	private double energy;           // energia
	private int duration_ms;         // duração, em milissegundos
	private double instrumentalness; // instrumentalidade
	private double valence;          // valência
	private int popularity;          // popularidade
	private double time;             // tempo
	private double liveness;         // vivacidade 
	private double loudness;         // volume
	private double speechiness;      // discurso
	private int year;                // ano
	
	private int i;                   // índice da string que apresenta todos os dados relacionados a uma música
	
	public Musica(String id, String name, String key, String[] artists, Date release_date, double acousticness, double danceability, double energy, int duration_ms, double instrumentalness, double valence, int popularity, double time, double liveness, double loudness, double speechiness, int year) {
		
		this.id = id;
		this.name = name;
		this.key = key;
		this.artists = artists;
		this.release_date = release_date;
		this.acousticness = acousticness;
		this.danceability = danceability;
		this.energy = energy;
		this.duration_ms = duration_ms;
		this.instrumentalness = instrumentalness;
		this.valence = valence;
		this.popularity = popularity;
		this.time = time;
		this.liveness = liveness;
		this.loudness = loudness;
		this.speechiness = speechiness;
		this.year = year;		
	}
	
	public Musica(String dados) {
		
		i = 0;
		this.valence = Double.parseDouble(getToken(dados));
		this.year = Integer.parseInt(getToken(dados));
		this.acousticness = Double.parseDouble(getToken(dados));
		
		this.artists = getArtistas(dados);
		
		this.danceability = Double.parseDouble(getToken(dados));
		this.duration_ms = Integer.parseInt(getToken(dados));
		this.energy = Double.parseDouble(getToken(dados));
		
		getToken(dados); // saltar campo da string original que não será utilizado
		
		this.id = getToken(dados);
		this.instrumentalness = Double.parseDouble(getToken(dados));
		this.key = getToken(dados);
		this.liveness = Double.parseDouble(getToken(dados));
		this.loudness = Double.parseDouble(getToken(dados));
		
		getToken(dados); // saltar campo da string original que não será utilizado
		
		this.name = getNome(dados);
		
		this.popularity = Integer.parseInt(getToken(dados));
		
		this.release_date = getData(getToken(dados));
		
		this.speechiness = Double.parseDouble(getToken(dados));
		this.time = Double.parseDouble(getToken(dados));
	}
	
	private String getNome(String dados) {
	
		String campo = "";
		boolean terminou = false;
		
		i+=2; // percorrendo a string até encontrar o início do texto.
	
		while (!terminou) {
			if ((dados.charAt(i) == ']') && (dados.charAt(i+1) == '\"') && (dados.charAt(i+2) == ',')) 
				terminou = true;
			else {
				campo += dados.charAt(i);
				i++;
			}
		}
			
		i+=3;
		
		return campo;
	}
	
	private Date getData(String dataString) {
		
		SimpleDateFormat formato;
		String[] dataCampos = dataString.split("-");
		Date data = new Date();
		
		if (dataCampos.length == 1) {
			dataString += "-01-01"; 
		}
		
		formato = new SimpleDateFormat("yyyy-MM-dd"); 
		try {
			data = formato.parse(dataString);
		} catch (ParseException excecao) {
			excecao.getMessage();
		}
		return data;
	}
	
	private String[] getArtistas (String dados) {
		
		String campo = "";
		String[] artistas;
		String nomeArtista = "";
		int inicio = 0;
		int fim;
		
		while (dados.charAt(i) != '[') // percorrendo a string até encontrar o início da lista de artistas.
			i++;
		
		i++;
		
		while (dados.charAt(i) != ']') { // percorrendo a string até encontrar o final da lista de artistas.
			campo += dados.charAt(i);
			i++;
		}
		
		while (dados.charAt(i) != ',') // percorrendo a string até encontrar o separador para o próximo campo.
			i++;
		
		i++;
		
		artistas = campo.split("\',"); // separador entre artistas: ',
	
		for (int j = 0; j < artistas.length; j++) {
			
			nomeArtista = "";
			inicio = 0;
			while ((artistas[j].charAt(inicio) == '\'') || (artistas[j].charAt(inicio) == ' ')) // percorrendo até encontrar o início do nome do artista.
				inicio++;
			
			for (fim = artistas[j].length()-1; (artistas[j].charAt(fim) == '\''); fim--);
			
			while (inicio <= fim) {
				nomeArtista += artistas[j].charAt(inicio);
				inicio++;
			}
			
			artistas[j] = nomeArtista;
		}
		
		return artistas;
	}
	
	private String getToken (String dados) {
	
		String campo = "";
		while ((i < dados.length()) && (dados.charAt(i) != ',')) {
			campo += dados.charAt(i);
			i++;
		}
		i++;
		return campo;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String[] getArtists() {
		return artists;
	}
	
	public void setArtists(String[] artists) {
		this.artists = artists;
	}
	
	public Date getRelease_date() {
		return release_date;
	}
	
	public void setRelease_date(Date release_date) {
		this.release_date = release_date;
	}
	
	public double getAcousticness() {
		return acousticness;
	}
	
	public void setAcousticness(double acousticness) {
		this.acousticness = acousticness;
	}
	
	public double getDanceability() {
		return danceability;
	}
	
	public void setDanceability(double danceability) {
		this.danceability = danceability;
	}
	
	public double getEnergy() {
		return energy;
	}
	
	public void setEnergy(double energy) {
		this.energy = energy;
	}
	
	public int getDuration_ms() {
		return duration_ms;
	}
	
	public void setDuration_ms(int duration_ms) {
		this.duration_ms = duration_ms;
	}
	
	public double getInstrumentalness() {
		return instrumentalness;
	}
	
	public void setInstrumentalness(double instrumentalness) {
		this.instrumentalness = instrumentalness;
	}
	
	public double getValence() {
		return valence;
	}
	
	public void setValence(double valence) {
		this.valence = valence;
	}
	
	public int getPopularity() {
		return popularity;
	}
	
	public void setPopularity(int popularity) {
		this.popularity = popularity;
	}
	
	public double getTime() {
		return time;
	}
	
	public void setTime(double time) {
		this.time = time;
	}
	
	public double getLiveness() {
		return liveness;
	}
	
	public void setLiveness(double liveness) {
		this.liveness = liveness;
	}
	
	public double getLoudness() {
		return loudness;
	}
	
	public void setLoudness(double loudness) {
		this.loudness = loudness;
	}
	
	public double getSpeechiness() {
		return speechiness;
	}
	
	public void setSpeechiness(double speechiness) {
		this.speechiness = speechiness;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public Musica clone() {
	
		Musica copia;
		Date release_date;
		String[] artists = new String[this.artists.length];
		
		for (int i = 0; i < this.artists.length; i++) {
			artists[i] = this.artists[i];
		}
		
		release_date = (Date)this.release_date.clone();
		
		copia = new Musica(this.id, this.name, this.key, artists, release_date, this.acousticness, this.danceability, this.energy, this.duration_ms, this.instrumentalness, this.valence, this.popularity, this.time, this.liveness, this.loudness, this.speechiness, this.year);
		
		return copia;
	}
	
	public void ler() {
	
		int qtdeArtistas;
		String dataString;
		
		System.out.print("Informe o id da musica: ");
		this.id = MyIO.readString();
		
		System.out.print("Informe o nome da música: ");
		this.name = MyIO.readLine();
		
		System.out.print("Informe a chave da música: ");
		this.key = MyIO.readString();
		
		System.out.print("Informe a quantidade de artistas associados a essa música: ");
		qtdeArtistas = MyIO.readInt();
		
		this.artists = new String[qtdeArtistas];
		for (int i = 0; i < qtdeArtistas; i++) {
			System.out.println("Informe o nome do " + (i + 1) + ".o artista associado a essa música: ");
			this.artists[i] = MyIO.readLine();
		}
		
		System.out.print("Informe a data de lançamento da música, no formato yyyy-MM-dd: ");
		dataString = MyIO.readString();
		this.release_date = getData(dataString);
		
		System.out.print("Informe a acústica da música: ");
		this.acousticness = MyIO.readDouble();
		
		System.out.print("Informe a dança da música: ");
		this.danceability = MyIO.readDouble();
		
		System.out.print("Informe a energia da música: ");
		this.energy = MyIO.readDouble();
		
		System.out.print("Informe a duração da música, em milissegundos: ");
		this.duration_ms = MyIO.readInt();
		
		System.out.print("Informe a instrumentabilidade da música: ");
		this.instrumentalness = MyIO.readDouble();
		
		System.out.print("Informe a valência da música: ");
		this.valence = MyIO.readDouble();
		
		System.out.print("Informe a popularidade da música: ");
		this.popularity = MyIO.readInt();
		
		System.out.print("Informe o tempo da música: ");
		this.time = MyIO.readDouble();
		
		System.out.print("Informe a vivacidade da música: ");
		this.liveness = MyIO.readDouble();
		
		System.out.print("Informe o volume da música: ");
		this.loudness = MyIO.readDouble();
		
		System.out.print("Informe o discurso da música: ");
		this.speechiness = MyIO.readDouble();
		
		System.out.print("Informe o ano da música: ");
		this.year = MyIO.readInt();
	}
	
	public void imprimir(int i) {
		
		int j;
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy"); 
		MyIO.print("["+i+"]");
		System.out.print(this.id + " ## [");
		for (j = 0; j < (this.artists.length - 1); j++) {
			System.out.print(this.artists[j] + ", ");
		}
		System.out.print(this.artists[j] + "] ## " + this.name + " ## ");
		System.out.print(formato.format(this.release_date));
		System.out.println(" ## " + this.acousticness + " ## " + this.danceability +  " ## " + this.instrumentalness + " ## " + this.liveness + " ## " + this.loudness + " ## " + this.speechiness + " ## " + this.energy + " ## " + this.duration_ms);
	}
	public void imprimir() {
		
		int j;
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy"); 
		
		
		for (j = 0; j < (this.artists.length - 1); j++) {
			System.out.print(this.artists[j] + ", ");
		}
		System.out.print(this.name);
		
	}
}
class Fila{
    public Musica[] filaMusicas = new  Musica[5];
    public int quantidadeMusicasFila;
  
    public Fila(){
      this.quantidadeMusicasFila = 0;
      //quantidadeMusicasFila = 0; 
    }
  
    public void adicionaMusicaNaFila(Musica pesquisado){
		if(quantidadeMusicasFila >= 5){
		  //remove a primeira musica da fila e libera uma posicção no final da fila
		  //RemoverUmaMusicaDaFila();
		  for(int i=0; i<4; i++){
		  filaMusicas[i]=filaMusicas[i+1];
		  }
		  quantidadeMusicasFila--;
		  //insere no fim da fila
		  filaMusicas[quantidadeMusicasFila] = pesquisado;
		  quantidadeMusicasFila ++;
		}else{
		  filaMusicas[quantidadeMusicasFila] = pesquisado;
		  quantidadeMusicasFila ++;
		}
	  } 
	  public String RemoverUmaMusicaDaFila(){
		String nomeMusicaRemovida = filaMusicas[0].getName();
		for(int i=0; i<4; i++){
		  filaMusicas[i]=filaMusicas[i+1];
		}
		quantidadeMusicasFila--;
		return nomeMusicaRemovida;
	  }
	
	  public void CalcularMedia(){
		double soma=0;
		int decimal,resultado=0;
		for(int i=0;i<quantidadeMusicasFila;i++){
		  resultado += filaMusicas[i].getDuration_ms();
		}
		soma=resultado;
		soma/=quantidadeMusicasFila;
	   
		decimal = (int)Math.round((soma - (int)soma) * 100);
		 resultado = resultado/quantidadeMusicasFila;
		 if (decimal>=50)
		 {
			 resultado++;
		 }
		MyIO.println(resultado);
	  
	  }
  }
public class Classe {

	public static int contarTotalMusicas() {
	
		int numMusicas = 0;
		ArquivoTextoLeitura arquivoMusicas = new ArquivoTextoLeitura("Tp3/dataAEDs.csv");
		
		while (arquivoMusicas.ler() != null)
			numMusicas++;
		
		arquivoMusicas.fecharArquivo();
		
		return numMusicas;
	}
	
	public static void lerMusicas(Musica[] listaMusicas) {
		
		ArquivoTextoLeitura arquivoMusicas = new ArquivoTextoLeitura("Tp3/dataAEDs.csv");
		int i = 0;
		String dadosMusica;
		
		dadosMusica = arquivoMusicas.ler();
		while ((dadosMusica = arquivoMusicas.ler()) != null) {
			
			listaMusicas[i] = new Musica(dadosMusica);
			i++;
		}
	
		arquivoMusicas.fecharArquivo();
	}






public static void pedro()
{
	String id;
        int p;
		int numTotalMusicas = contarTotalMusicas();
    Fila filaDeMusicas = new Fila();
		Musica[] listaMusicas = new Musica[numTotalMusicas];
		Musica pesquisado;
		PesquisaSequencial pesquisa = new PesquisaSequencial();
		lerMusicas(listaMusicas);
		id = MyIO.readLine();
  do{
	  //id = MyIO.readLine();
    //lendo os ids que serão inseridos na fila
    pesquisado = pesquisa.buscarId(listaMusicas,id);
    filaDeMusicas.adicionaMusicaNaFila(pesquisado);
    filaDeMusicas.CalcularMedia();
	id = MyIO.readLine();
  }while(!id.equals("FIM"));
  p=MyIO.readInt();//ler a quantidade de instruções que vai fazer

  String instrucao;
  
  		for (int k=0;k<p;k++)
		{

         instrucao = MyIO.readLine();
         if(instrucao.charAt(0) == 'R'){
          String nomeMusica = "";
          nomeMusica = filaDeMusicas.RemoverUmaMusicaDaFila();
          MyIO.println("(R) "+nomeMusica);
         } else{
          //adicionar e calcularMedia
          String novoString="";//novo string = id
          for (int v=2;v<24;v++)
			    {
				    novoString+=instrucao.charAt(v);
			    }
          pesquisado=pesquisa.buscarId(listaMusicas, novoString);
          filaDeMusicas.adicionaMusicaNaFila(pesquisado);
          filaDeMusicas.CalcularMedia();
         }
    }
  }
  static class Lista{

 public String[] TodosId = new String[1000];
 public int quantidade; 
 public String[] Aux = new String[1000];
 public Lista()
 {
	quantidade=0;
 }

public void adicionarMusicaNaLista(String id)
{
   TodosId[quantidade]=id;
   Aux[quantidade]=id;
   quantidade++;
   
}
public void adicionarMusicaNoComeco(String id)
{
	int c=1;
	quantidade++;
	for (int i=0;i<quantidade;i++)
	{
       TodosId[c]=Aux[i];
	   c++;
	}
	TodosId[0]=id;
	for (int i=0;i<quantidade;i++)
	{
        Aux[i]=TodosId[i];
	}

}
public void adicionarMusicaNoFinal(String id)
{
	
	TodosId[quantidade]=id;
	Aux[quantidade]=id;
	quantidade++;

}
public String removerEmTalPosicao(int posicao)
{
	String id=TodosId[posicao];
 for (int i=posicao;i<quantidade-1;i++)
 {
	 TodosId[i]=TodosId[i+1];
	 Aux[i]=Aux[i+1];
 }
 quantidade--;

 return id;
}
public String removerNoInicio()
{
	String id=TodosId[0];
	quantidade--;
	for (int i=0;i<quantidade;i++)
	{
      TodosId[i]=TodosId[i+1];
	  Aux[i]=Aux[i+1];
	}
	return id;
}
public String removerNoFinal()
{
	quantidade--;
  String id=TodosId[quantidade];
  
  
  return id;
}
public void adicionarEmTalPosicao(String id,int posicao)
{
 quantidade++;
 for (int i=posicao;i<quantidade;i++)
 {
	 TodosId[i+1]=Aux[i];
	
 }
 TodosId[posicao]=id;
 for (int i=0;i<quantidade;i++)
 {
	 Aux[i]=TodosId[i];
 }
}
  }





  








  

	public static void main(String[] args) {
	//pedro();
	String nome_musica;

	int numTotalMusicas = contarTotalMusicas();
	Lista  ListaDeMuSICA= new Lista();
	Musica[] listaMusicas = new Musica[numTotalMusicas];
	Musica pesquisado;
	//int contadorDeMusica=0;
	//String StringComOnomeDeDasMusicasRemovida;
	String[] TodosId=new String[100];
	PesquisaSequencial pesquisa = new PesquisaSequencial();
	String id;
	int p;
	lerMusicas(listaMusicas);
	
	id = MyIO.readLine();
	while (!id.equals("FIM")) {
		ListaDeMuSICA.adicionarMusicaNaLista(id);
		//pesquisado = pesquisa.buscarId(listaMusicas, id);
		
	//pesquisado.imprimir(i);
	
		id = MyIO.readLine();
	}
	String instrucao;
	p=MyIO.readInt();
	for (int k=0;k<p;k++)
	{
        instrucao=MyIO.readLine();
		if (instrucao.charAt(0)=='I' && instrucao.charAt(1)=='I')
		{
			String novoString="";//novo string = id
			for (int v=3;v<25;v++)
				  {
					  novoString+=instrucao.charAt(v);
				  }
				  ListaDeMuSICA.adicionarMusicaNoComeco(novoString);
		}
		else if(instrucao.charAt(0)=='I' && instrucao.charAt(1)=='F')
		{
			String novoString="";//novo string = id
			for (int v=3;v<25;v++)
				  {
					  novoString+=instrucao.charAt(v);
				  }
				  ListaDeMuSICA.adicionarMusicaNoFinal(novoString);
		}
		else if(instrucao.charAt(0)=='R' && instrucao.charAt(1)=='*')
		{
			String novoString="";//novo string = id
         char myChar = instrucao.charAt(3);
        int myInt = Character.getNumericValue(myChar);  
		char pedro = instrucao.charAt(4);
        int pedro1 = Character.getNumericValue(pedro);  
		myInt=(myInt*10)+pedro1; 
        pesquisado = pesquisa.buscarId(listaMusicas,ListaDeMuSICA.removerEmTalPosicao(myInt));
		nome_musica=pesquisado.getName();
		MyIO.println("(R) "+nome_musica);
		}
		else if(instrucao.charAt(0)=='R' && instrucao.charAt(1)=='I')
		{
			pesquisado = pesquisa.buscarId(listaMusicas,ListaDeMuSICA.removerNoInicio());
			nome_musica=pesquisado.getName();
			MyIO.println("(R) "+nome_musica);
		}
		else if(instrucao.charAt(0)=='R' && instrucao.charAt(1)=='F')
		{
			pesquisado = pesquisa.buscarId(listaMusicas,ListaDeMuSICA.removerNoFinal());
			nome_musica=pesquisado.getName();
			MyIO.println("(R) "+nome_musica);
		}
		else if(instrucao.charAt(0)=='I' && instrucao.charAt(1)=='*')
		{
          
			char myChar = instrucao.charAt(3);
			int myInt = Character.getNumericValue(myChar);  
			char pedro = instrucao.charAt(4);
			int pedro1 = Character.getNumericValue(pedro);  
			myInt=(myInt*10)+pedro1;
			String novoString="";
			for (int v=6;v<28;v++)
			{
				novoString+=instrucao.charAt(v);
			}
			ListaDeMuSICA.adicionarEmTalPosicao(novoString,myInt);

		}
	
		
	}
	int x=ListaDeMuSICA.quantidade;
	for (int i=0;i<x;i++)
	{
		
		id=ListaDeMuSICA.TodosId[i];
		pesquisado = pesquisa.buscarId(listaMusicas, id);
		pesquisado.imprimir(i);
	}
}
  }

