package br.com.nocaute.image;

import javax.swing.ImageIcon;

public final class MasterImage extends ImageIcon{
	private static final long serialVersionUID = -1243931450819789591L;

	public static final MasterImage
	search_22x22 = LoadImage("22x22/localizar.png");

	public static final MasterImage	
	add_22x22 = LoadImage("22x22/adicionar.png");
	
	public static final MasterImage
	remove_22x22 = LoadImage("22x22/remover.png");
	
	public static final MasterImage
	save_22x22 = LoadImage("22x22/salvar.png");
	
	public static final MasterImage
	ok_13x13 = LoadImage("13x13/ok.png");
	
	public static final MasterImage
	user_16x16 = LoadImage("16x16/user.png");
	
	public static final MasterImage
	financial_16x16 = LoadImage("16x16/financeiro.png");
	
	private MasterImage(final String titulo) {
		super(MasterImage.class.getResource(titulo));
	}
	
	/**
	 * Carrega a imagem de acordo com o texto passado por
	 * parametro.
	 * @param tituloIcone
	 * @return
	 */
	
	public static MasterImage LoadImage(final String tituloIcone) {
		return new MasterImage(tituloIcone);
	}
}

