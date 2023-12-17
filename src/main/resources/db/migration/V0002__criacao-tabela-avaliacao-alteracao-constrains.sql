CREATE TABLE public.avaliacao (
    avaliacao_id uuid NOT NULL,
    avaliacao character varying(5000),
    data_cadastro timestamp without time zone NOT NULL,
    apreciador_id uuid,
    mestre_cervejeiro_id uuid,
    troca_id uuid
);


ALTER TABLE public.avaliacao OWNER TO postgres;

ALTER TABLE troca
DROP CONSTRAINT troca_status_check;

ALTER TABLE troca
ADD CONSTRAINT troca_status_check
CHECK (status::text = ANY (ARRAY['ACEITO'::character varying::text, 'RECUSADO'::character varying::text, 'PENDENTE'::character varying::text, 'ENTREGUE'::character varying::text]));