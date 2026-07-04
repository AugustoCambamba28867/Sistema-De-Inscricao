package sdpfrequencia.dao;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import sdpfrequencia.modelo.FichaInscricao;

public class FichaInscricaoDAO {

    private static final String FILE_NAME = "dados.dat";

    public void salvar(FichaInscricao ficha) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(FILE_NAME, "rw")) {
            raf.seek(raf.length());

            // CURSO
            raf.writeUTF(ficha.getCurso() != null ? ficha.getCurso() : "");
            raf.writeUTF(ficha.getHorario() != null ? ficha.getHorario() : "");

            // FORMANDO
            raf.writeUTF(ficha.getNome() != null ? ficha.getNome() : "");
            raf.writeUTF(ficha.getMorada() != null ? ficha.getMorada() : "");
            raf.writeUTF(ficha.getLocalidade() != null ? ficha.getLocalidade() : "");
            raf.writeUTF(ficha.getMunicipio() != null ? ficha.getMunicipio() : "");
            raf.writeUTF(ficha.getTelefone() != null ? ficha.getTelefone() : "");
            raf.writeUTF(ficha.getTelemovel() != null ? ficha.getTelemovel() : "");
            raf.writeUTF(ficha.getEmail() != null ? ficha.getEmail() : "");
            raf.writeUTF(ficha.getNascimento() != null ? ficha.getNascimento() : "");
            raf.writeUTF(ficha.getSexo() != null ? ficha.getSexo() : "");

            // ENTIDADE
            raf.writeUTF(ficha.getEpNome() != null ? ficha.getEpNome() : "");
            raf.writeUTF(ficha.getEpMorada() != null ? ficha.getEpMorada() : "");
            raf.writeUTF(ficha.getEpLocalidade() != null ? ficha.getEpLocalidade() : "");
            raf.writeUTF(ficha.getEpMunicipio() != null ? ficha.getEpMunicipio() : "");
            raf.writeUTF(ficha.getEpTelefone() != null ? ficha.getEpTelefone() : "");
            raf.writeUTF(ficha.getEpTelemovel() != null ? ficha.getEpTelemovel() : "");
            raf.writeUTF(ficha.getEpFax() != null ? ficha.getEpFax() : "");
            raf.writeUTF(ficha.getEpEmail() != null ? ficha.getEpEmail() : "");
            raf.writeUTF(ficha.getEpNif() != null ? ficha.getEpNif() : "");

            // RH
            raf.writeUTF(ficha.getRhNome() != null ? ficha.getRhNome() : "");
            raf.writeUTF(ficha.getRhTelefone() != null ? ficha.getRhTelefone() : "");
            raf.writeUTF(ficha.getRhTelemovel() != null ? ficha.getRhTelemovel() : "");
            raf.writeUTF(ficha.getRhEmail() != null ? ficha.getRhEmail() : "");
        }
    }

    public List<FichaInscricao> listarTodas() throws IOException {
        List<FichaInscricao> lista = new ArrayList<>();

        try (RandomAccessFile raf = new RandomAccessFile(FILE_NAME, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                FichaInscricao ficha = new FichaInscricao();

                // CURSO
                ficha.setCurso(raf.readUTF());
                ficha.setHorario(raf.readUTF());

                // FORMANDO
                ficha.setNome(raf.readUTF());
                ficha.setMorada(raf.readUTF());
                ficha.setLocalidade(raf.readUTF());
                ficha.setMunicipio(raf.readUTF());
                ficha.setTelefone(raf.readUTF());
                ficha.setTelemovel(raf.readUTF());
                ficha.setEmail(raf.readUTF());
                ficha.setNascimento(raf.readUTF());
                ficha.setSexo(raf.readUTF());

                // ENTIDADE
                ficha.setEpNome(raf.readUTF());
                ficha.setEpMorada(raf.readUTF());
                ficha.setEpLocalidade(raf.readUTF());
                ficha.setEpMunicipio(raf.readUTF());
                ficha.setEpTelefone(raf.readUTF());
                ficha.setEpTelemovel(raf.readUTF());
                ficha.setEpFax(raf.readUTF());
                ficha.setEpEmail(raf.readUTF());
                ficha.setEpNif(raf.readUTF());

                // RH
                ficha.setRhNome(raf.readUTF());
                ficha.setRhTelefone(raf.readUTF());
                ficha.setRhTelemovel(raf.readUTF());
                ficha.setRhEmail(raf.readUTF());

                lista.add(ficha);
            }
        } catch (java.io.FileNotFoundException e) {
            // Se o ficheiro ainda não existe, retorna lista vazia em vez de dar erro
            System.out.println("Ficheiro ainda não existe. Será criado ao salvar.");
        }

        return lista;
    }
}
