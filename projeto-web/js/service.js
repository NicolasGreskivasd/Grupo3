const API_URL = 'http://192.168.239.129/:30001'; // URL base para o backend

// Função genérica para fazer chamadas GET
async function getData(endpoint) {
    try {
        const response = await fetch(`${API_URL}/${endpoint}`);
        return await response.json();
    } catch (error) {
        console.error('Erro ao buscar dados:', error);
        return null;
    }
}

// Função genérica para criar (POST) ou atualizar (PUT) dados
async function sendData(endpoint, data, method = 'POST') {
    try {
        console.log(data);
        const response = await fetch(`${API_URL}/${endpoint}`, {
            method: method,
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        });

        if (response.ok) {
            return await response.json();
        } else {
            throw new Error('Erro ao cadastrar usuário');
        }
    } catch (error) {
        console.error('Erro ao enviar dados:', error);
        return null;
    }
}

// Função para excluir dados (DELETE)
async function deleteData(endpoint) {
    try {
        const response = await fetch(`${API_URL}/${endpoint}`, {
            method: 'DELETE',
        });
        return response.ok;
    } catch (error) {
        console.error('Erro ao deletar dados:', error);
        return false;
    }
}
